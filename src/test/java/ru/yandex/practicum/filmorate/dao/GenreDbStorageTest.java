package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    public void getGenreByIdTest() {
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        assertEquals(genreStorage.getGenreById(1).getName(), "Комедия");
        assertEquals(genreStorage.getGenreById(3).getName(), "Мультфильм");
    }

    @Test
    @DirtiesContext
    public void getGenres() {
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(1, "Комедия"));
        genreList.add(new Genre(2, "Драма"));
        genreList.add(new Genre(3, "Мультфильм"));
        genreList.add(new Genre(4, "Триллер"));
        genreList.add(new Genre(5, "Документальный"));
        genreList.add(new Genre(6, "Боевик"));
        assertEquals(genreList, genreStorage.getGenres());
    }

    @Test
    @DirtiesContext
    public void getGenresByFilmID() {
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        filmStorage.addFilmToStorage(newFilm);
        assertTrue(newFilm.getGenres().isEmpty());
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genreStorage.getGenreById(1));
        genres.add(genreStorage.getGenreById(3));
        newFilm.setGenres(genres);
        genreStorage.addGenresByFilm(newFilm);
        assertEquals(new ArrayList<>(genres), genreStorage.getGenresByFilmID(newFilm.getId()));
    }

    @Test
    @DirtiesContext
    public void deleteGenresByFilm() {
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        filmStorage.addFilmToStorage(newFilm);
        assertTrue(newFilm.getGenres().isEmpty());
        HashSet<Genre> genres = new HashSet<>();
        genres.add(genreStorage.getGenreById(1));
        genres.add(genreStorage.getGenreById(3));
        newFilm.setGenres(genres);
        genreStorage.addGenresByFilm(newFilm);
        assertEquals(new ArrayList<>(genres), genreStorage.getGenresByFilmID(newFilm.getId()));
        genreStorage.deleteGenresByFilm(newFilm);
        assertTrue(genreStorage.getGenresByFilmID(newFilm.getId()).isEmpty());
    }
}
