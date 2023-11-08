package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void addFilmToStorage(Film film);

    void updateFilmInStorage(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);
}
