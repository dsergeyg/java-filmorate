package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    public static final LocalDate MINDATE = LocalDate.of(1895, 12, 28);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private int idSequence = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на создание User " + film);
        try {
            return filmController(film, true);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException("Bad request: " + e.getMessage());
        }
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на обновление User " + film);
        try {
            return filmController(film, false);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException("Bad request");
        }
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private Film filmController(Film film, boolean isCreate) throws ValidationException {
        Optional<ConstraintViolation<Film>> violation = validator.validate(film).stream().findFirst();
        if (violation.isPresent()) {
            throw new ValidationException(violation.get().getMessage());
        }
        if (!film.getReleaseDate().isAfter(MINDATE.minusDays(1)))
            throw new ValidationException("Release date may not be before " + formatter.format(LocalDateTime.now()) + " " + film);
        if (isCreate) {
            film.setId(++idSequence);
        }
        else {
            if (!films.containsKey(film.getId())) {
                throw new ValidationException("Object not found " + film);
            }
        }
        films.put(film.getId(), film);
        return film;
    }
}
