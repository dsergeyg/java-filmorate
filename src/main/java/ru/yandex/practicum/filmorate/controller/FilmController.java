package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;
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
        return filmService.filmController(film, true);
    }

    @PutMapping("/films")
    public Film putFilm(@Validated(Update.class) @RequestBody Film film) {
        return filmService.filmController(film, false);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable String id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikeToFilm(@PathVariable String id, @PathVariable String userId) {
        return filmService.likeController(id, userId, true);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLikeFromFilm(@PathVariable String id, @PathVariable String userId) {
        return filmService.likeController(id, userId, false);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilm(@RequestParam(name = "count", required = false, defaultValue = "10") String count) {
        return filmService.getCountPopularFilms(count);
    }
}
