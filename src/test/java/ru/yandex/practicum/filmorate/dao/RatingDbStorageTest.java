package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    public void getRatingByIdTest() {
        RatingDbStorage ratingStorage = new RatingDbStorage(jdbcTemplate);
        assertEquals(ratingStorage.getRatingById(1).getName(), "G");
        assertEquals(ratingStorage.getRatingById(3).getName(), "PG-13");
    }

    @Test
    @DirtiesContext
    public void getRatingsTest() {
        RatingDbStorage ratingStorage = new RatingDbStorage(jdbcTemplate);
        List<Rating> ratingList = new ArrayList<>();
        ratingList.add(new Rating(1, "G"));
        ratingList.add(new Rating(2, "PG"));
        ratingList.add(new Rating(3, "PG-13"));
        ratingList.add(new Rating(4, "R"));
        ratingList.add(new Rating(5, "NC-17"));
        assertEquals(ratingList, ratingStorage.getRatings());
    }
}
