package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface FilmStorage {

    void addFilmToStorage(Film film);

    void updateFilmInStorage(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    Film addLike(long id, long userId);

    Film deleteLike(long id, long userId);

    List<Long> getLikes(long id);

    void filmCheck(long id);

    Rating getRatingById(long id);

    List<Rating> getRatings();

    Genre getGenreById(long id);

    List<Genre> getGenres();

    List<Long> getGenresByFilmID(long id);

    void addGenresByFilm(Film film);

}
