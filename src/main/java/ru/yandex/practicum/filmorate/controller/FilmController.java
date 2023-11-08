package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.*;
import java.util.*;

@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
       this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@Validated(Update.class) @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLikeFromFilm(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilm(@RequestParam(name = "count", required = false, defaultValue ="10") int count) {
        return filmService.getCountPopularFilms(count);
    }
}
