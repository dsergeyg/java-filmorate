package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UtilService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    private Film film;
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(new InMemoryUserStorage())));
        film = new Film();
        film.setName("SomeFilm");
        film.setDescription("SomeDescription");
        film.setReleaseDate(UtilService.MIN_FILM_DATE);
        film.setDuration(90);
    }

    @Test
    void postFilm() {
        assertDoesNotThrow(() -> filmController.postFilm(film));
        assertTrue(validator.validate(film).stream().findFirst().isEmpty());
        film.setName(null);
        assertFalse(validator.validate(film).stream().findFirst().isEmpty());
        film.setName(" ");
        assertFalse(validator.validate(film).stream().findFirst().isEmpty());
        film.setName("SomeFilm");
        film.setDescription("a".repeat(201));
        assertFalse(validator.validate(film).stream().findFirst().isEmpty());
        film.setDescription("SomeDescription");
        film.setReleaseDate(UtilService.MIN_FILM_DATE.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
        film.setReleaseDate(UtilService.MIN_FILM_DATE.plusDays(1));
        assertDoesNotThrow(() -> filmController.postFilm(film));
        assertTrue(filmController.getFilms().contains(film));
    }

    @Test
    void putFilm() {
        Film curFilm = filmController.postFilm(film);

        assertTrue(filmController.getFilms().contains(film));
        assertDoesNotThrow(() -> filmController.putFilm(curFilm));
        curFilm.setName(null);
        assertFalse(validator.validate(curFilm).stream().findFirst().isEmpty());
        curFilm.setName(" ");
        assertFalse(validator.validate(curFilm).stream().findFirst().isEmpty());
        curFilm.setName("SomeFilm");
        curFilm.setDescription("a".repeat(201));
        assertFalse(validator.validate(curFilm).stream().findFirst().isEmpty());
        curFilm.setDescription("SomeDescription");
        curFilm.setReleaseDate(UtilService.MIN_FILM_DATE.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.postFilm(curFilm));
        curFilm.setReleaseDate(UtilService.MIN_FILM_DATE.plusDays(1));
        assertDoesNotThrow(() -> filmController.putFilm(curFilm));
        assertTrue(filmController.getFilms().contains(curFilm));
    }

    @Test
    void getFilms() {
        Film curFilm = new Film();
        curFilm.setName("SomeFilm1");
        curFilm.setDescription("SomeDescription1");
        curFilm.setReleaseDate(UtilService.MIN_FILM_DATE);
        curFilm.setDuration(110);

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