package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private Film film;
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = Film.builder().setName("SomeFilm")
                .setDescription("SomeDescription")
                .setReleaseDate(FilmController.MINDATE)
                .setDuration(90).build();
    }

    @Test
    void postFilm() {
        assertDoesNotThrow(() -> filmController.postFilm(film));
        film.setName(null);
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        film.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        film.setName("SomeFilm");
        film.setDescription("a".repeat(201));
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        film.setDescription("SomeDescription");
        film.setReleaseDate(FilmController.MINDATE.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        film.setReleaseDate(FilmController.MINDATE.plusDays(1));
        assertDoesNotThrow(() -> filmController.postFilm(film));
        assertTrue(filmController.getFilms().contains(film));
    }

    @Test
    void putFilm() {
        Film curFilm = filmController.postFilm(film);

        assertTrue(filmController.getFilms().contains(film));
        assertDoesNotThrow(() -> filmController.putFilm(curFilm));
        curFilm.setName(null);
        assertThrows(ValidationException.class, () -> filmController.putFilm(curFilm));
        curFilm.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.postFilm(curFilm));
        curFilm.setName("SomeFilm");
        curFilm.setDescription("a".repeat(201));
        assertThrows(ValidationException.class, () -> filmController.postFilm(curFilm));
        curFilm.setDescription("SomeDescription");
        curFilm.setReleaseDate(FilmController.MINDATE.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.postFilm(curFilm));
        curFilm.setReleaseDate(FilmController.MINDATE.plusDays(1));
        assertDoesNotThrow(() -> filmController.postFilm(curFilm));
        assertTrue(filmController.getFilms().contains(curFilm));
    }

    @Test
    void getFilms() {
        Film curFilm = Film.builder().setName("SomeFilm1")
                .setDescription("SomeDescription1")
                .setReleaseDate(FilmController.MINDATE)
                .setDuration(110).build();

        List<Film> listFilm = new ArrayList<>();
        listFilm.add(filmController.postFilm(film));
        listFilm.add(filmController.postFilm(curFilm));
        assertEquals(listFilm, filmController.getFilms());
        curFilm.setName("SomeFilm2");
        curFilm.setDescription("SomeDescription2");
        curFilm.setReleaseDate(LocalDate.now());
        filmController.putFilm(curFilm);
        assertEquals(listFilm, filmController.getFilms());
    }
}