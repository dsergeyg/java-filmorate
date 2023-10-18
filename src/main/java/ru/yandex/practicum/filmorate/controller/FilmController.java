package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film, BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors())
                log.error(fieldError.getDefaultMessage());
        }
        log.info("Получен запрос на создание" + film);
        int id = film.getId();
        films.put(id, film);
        return films.get(id);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError fieldError : result.getFieldErrors())
                log.error(fieldError.getDefaultMessage());
        }
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            return films.get(id);
        } else {
            log.info(film + " Not found!");
            return null;
        }
    }

    @GetMapping("/films")
    public List<Film> listFilms() {
        return new ArrayList<>(films.values());
    }
}
