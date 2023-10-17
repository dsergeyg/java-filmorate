package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @Test
    void validName() {
        assertDoesNotThrow(() -> new Film(null, "SomeFilm", "Description of new film", LocalDate.of(2020, 10, 1), 90));
        assertThrows(NullPointerException.class, () -> new Film(null, null, "Description of new film", LocalDate.of(2020, 10, 1), 90));
        Optional<ConstraintViolation<Film>> violation = validator.validate(new Film(null, "", "Description of new film", LocalDate.of(2020, 10, 1), 90)).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Название не может быть пустым", violation.get().getMessage());
    }

    @Test
    void validDescription() {
        String description = "a".repeat(201);
        Optional<ConstraintViolation<Film>> violation = validator.validate(new Film(null, "SomeFilm", description, LocalDate.of(2020, 10, 1), 90)).stream().findFirst();
        assertFalse(violation.isEmpty());
        assertEquals("Максимальная длина описания — 200 символов", violation.get().getMessage());
        description = "a".repeat(200);
        violation = validator.validate(new Film(null, "SomeFilm", description, LocalDate.of(2020, 10, 1), 90)).stream().findFirst();
        assertTrue(violation.isEmpty());
    }

    @Test
    void validReleaseDate() {
        assertDoesNotThrow(() -> new Film(null, "SomeFilm", "Description of new film", LocalDate.of(2020, 10, 1), 90));
        assertThrows(NullPointerException.class, () -> new Film(null, "SomeFilm", "Description of new film", null, 90));
        assertDoesNotThrow(() -> new Film(null, "SomeFilm", "Description of new film", Film.MINDATE, 90));
        assertThrows(ValidationException.class,() -> new Film(null, "SomeFilm", "Description of new film", Film.MINDATE.minusDays(1), 90));
    }

    @Test
    void validDuration() {
        assertThrows(ValidationException.class, () -> new Film(null, "SomeFilm", "SomeDescription", LocalDate.of(2020, 10, 1), -100));
        try {
            new Film(null, "SomeFilm", "SomeDescription", LocalDate.of(2020, 10, 1), -1);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть положительной", e.getMessage());
        }
        try {
            new Film(null, "SomeFilm", "SomeDescription", LocalDate.of(2020, 10, 1), 0);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть положительной", e.getMessage());
        }
        assertDoesNotThrow(() -> new Film(null, "SomeFilm", "SomeDescription", LocalDate.of(2020, 10, 1), 1));
    }

    @AfterAll
    static void factoryClose() {
        factory.close();
    }

}