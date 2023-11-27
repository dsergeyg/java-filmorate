package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE genre_id = ?", id);
        if (sqlRowSet.next()) {
            Genre genre = new Genre(
                    sqlRowSet.getLong("genre_id"),
                    sqlRowSet.getString("name"));
            log.info("Найден жанр фильма: {} {}", genre.getId(), genre.getName());
            return genre;
        } else {
            log.info("Жанр фильма с идентификатором {} не найден.", id);
            throw new NotFoundException("Жанр id = " + id + " не найден!");
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT genre_id, name FROM genre ORDER BY genre_id ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public List<Genre> getGenresByFilmID(long id) {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM film_genre AS fg " +
                "INNER JOIN genre AS g ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?" +
                "ORDER BY g.genre_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public void addGenresByFilm(Film film) {
        deleteGenresByFilm(film);

        String sqlInsert = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlInsert,
                    film.getId(),
                    genre.getId());
        }
        film.getGenres().clear();
        film.getGenres().addAll(getGenresByFilmID(film.getId()));
        log.info("Жанры для фильма добавлены: {}", film.getId());
    }

    @Override
    public void deleteGenresByFilm(Film film) {
        String sqlDelete = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlDelete, film.getId());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        long id = rs.getLong("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
