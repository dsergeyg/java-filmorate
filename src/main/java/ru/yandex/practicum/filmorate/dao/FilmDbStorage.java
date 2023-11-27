package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
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
        String sqlInsert = "INSERT INTO film_data (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlInsert, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Фильм добавлен: {}", film.getName());
    }

    @Override
    public void updateFilmInStorage(Film film) {
        getFilmById(film.getId());
        String sqlUpdate = "UPDATE film_data SET name = ?, description = ?, release_date = ?, duration = ?,  rating_id = ? WHERE film_id = ?";

        jdbcTemplate.update(sqlUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        log.info("Фильм обновлен: {} {}", film.getId(), film.getName());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f. rating_id, m.name AS mpa_name " +
                "FROM film_data f LEFT JOIN mpa_rating m ON f.rating_id = m.rating_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        Rating rating = new Rating(rs.getLong("rating_id"), rs.getString("mpa_name"));
        Film film = new Film(id, name, description, releaseDate, duration, rating, new HashSet<>(), new HashSet<>(getLikes(id)));
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rating_id, m.name AS mpa_name" +
                " FROM film_data AS f LEFT JOIN mpa_rating AS m ON f.rating_id = m.rating_id" +
                " WHERE film_id = ? LIMIT 1", id);

        if (sqlRowSet.next()) {
            Film film = new Film(
                    sqlRowSet.getLong("film_id"),
                    sqlRowSet.getString("name"),
                    sqlRowSet.getString("description"),
                    sqlRowSet.getDate("release_date").toLocalDate(),
                    sqlRowSet.getLong("duration"),
                    new Rating(sqlRowSet.getLong("rating_id"),sqlRowSet.getString(7)),
                    new HashSet<>(),
                    new HashSet<>(getLikes(id)));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм id = " + id + " не найден!");
        }
    }

    @Override
    public Film addLike(long id, long userId) {
        getFilmById(id);
        String sqlInsert = "INSERT INTO like_list (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlInsert, id, userId);
        log.info("Лайк добавлен: {} {}", id, userId);
        return getFilmById(id);
    }

    @Override
    public Film deleteLike(long id, long userId) {
        getFilmById(id);
        String sqlDelete = "DELETE FROM like_list WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlDelete, id, userId);
        log.info("Лайк удален: {} {}", id, userId);
        return getFilmById(id);
    }

    @Override
    public List<Long> getLikes(long id) {
        String sql = "SELECT user_id FROM like_list WHERE film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), id);
    }
}
