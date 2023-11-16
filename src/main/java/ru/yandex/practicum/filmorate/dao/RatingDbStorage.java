package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("ratingDbStorage")
@Slf4j
public class RatingDbStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating getRatingById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating WHERE rating_id = ?", id);

        if (sqlRowSet.next()) {
            Rating rating = new Rating(
                    sqlRowSet.getLong("rating_id"),
                    sqlRowSet.getString("name"));
            log.info("Найден рейтинг: {} {}", rating.getId(), rating.getName());
            return rating;
        } else {
            log.info("рейтинг с идентификатором {} не найден.", id);
            throw new NotFoundException("Рейтинг id = " + id + " не найден!");
        }
    }

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT rating_id, name FROM mpa_rating ORDER BY rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        long id = rs.getLong("rating_id");
        String name = rs.getString("name");
        return new Rating(id, name);
    }
}
