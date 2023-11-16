package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilmToStorage(Film film) {
        String sqlInsert = "INSERT INTO film_data (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?);";

        jdbcTemplate.update(sqlInsert,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingId());
        if (!film.getGenreList().isEmpty()) {
            addGenresByFilm(film);
        }
        log.info("Фильм добавлен: {}", film.getName());
    }

    @Override
    public void updateFilmInStorage(Film film) {
        filmCheck(film.getId());
        String sqlUpdate = "UPDATE film_data SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?;";

        jdbcTemplate.update(sqlUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        if (!film.getGenreList().isEmpty()) {
            addGenresByFilm(film);
        }
        log.info("Фильм обновлен: {} {}", film.getId(), film.getName());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM film_data;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        long ratingId = rs.getLong("rating_id");
        Film film = new Film(id, name, description, releaseDate, duration, ratingId, new HashSet<>(getGenresByFilmID(id)), new HashSet<>(getLikes(id)));
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film_data WHERE film_id = ? LIMIT 1;", id);

        if (sqlRowSet.next()) {
            Film film = new Film(
                    sqlRowSet.getLong("film_id"),
                    sqlRowSet.getString("name"),
                    sqlRowSet.getString("description"),
                    sqlRowSet.getDate("release_date").toLocalDate(),
                    sqlRowSet.getLong("duration"),
                    sqlRowSet.getLong("rating_id"),
                    new HashSet<>(),
                    new HashSet<>(getLikes(id)));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public Film addLike(long id, long userId) {
        filmCheck(id);
        String sqlInsert = "INSERT INTO like_list (film_id, user_id) VALUES (?, ?);";

        jdbcTemplate.update(sqlInsert, id, userId);
        log.info("Лайк добавлен: {} {}", id, userId);
        return getFilmById(id);
    }

    @Override
    public Film deleteLike(long id, long userId) {
        filmCheck(id);
        String sqlDelete = "DELETE FROM like_list WHERE film_id = ? AND user_id = ?;";

        jdbcTemplate.update(sqlDelete, id, userId);
        log.info("Лайк удален: {} {}", id, userId);
        return getFilmById(id);
    }

    @Override
    public List<Long> getLikes(long id) throws NotFoundException {
        String sql = "SELECT user_id FROM like_list WHERE film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), id);
    }

    @Override
    public void filmCheck(long id) throws NotFoundException {
        if (getFilmById(id) == null)
            throw new NotFoundException("Фильм id = " + id + " не найден!");
    }

    @Override
    public Rating getRatingById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM map_rating WHERE id = ?;", id);

        if (sqlRowSet.next()) {
            Rating rating = new Rating(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name"));
            log.info("Найден рейтинг: {} {}", rating.getId(), rating.getName());
            return rating;
        } else {
            log.info("рейтинг с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT id, name FROM mpa_rating";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Rating(id, name);
    }

    @Override
    public Genre getGenreById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE id = ?;", id);

        if (sqlRowSet.next()) {
            Genre genre = new Genre(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name"));
            log.info("Найден жанр фильма: {} {}", genre.getId(), genre.getName());
            return genre;
        } else {
            log.info("Жанр фильма с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT id, name FROM genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public List<Long> getGenresByFilmID(long id) {
        String sql = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("genre_id"), id);
    }

    @Override
    public void addGenresByFilm(Film film) {
        String sqlDelete = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sqlDelete, film.getId());

        String sqlInsert = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";

        for (Long id : film.getGenreList()) {
            jdbcTemplate.update(sqlInsert,
                    film.getId(),
                    id);
        }
        log.info("Жанры для фильма добавлены: {}", film.getId());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

}
