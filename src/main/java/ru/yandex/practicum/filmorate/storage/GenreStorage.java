package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenreById(long id);

    List<Genre> getGenres();

    List<Genre> getGenresByFilmID(long id);

    void addGenresByFilm(Film film);

    void deleteGenresByFilm(Film film);


}
