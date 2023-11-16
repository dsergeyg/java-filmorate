package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long idSequence;

    @Override
    public void addFilmToStorage(Film film) {
        film.setId(++idSequence);
        films.put(film.getId(), film);
    }

    @Override
    public Rating getRatingById(long id) {
        return null;
    }

    @Override
    public List<Rating> getRatings() {
        return null;
    }

    @Override
    public Genre getGenreById(long id) {
        return null;
    }

    @Override
    public List<Genre> getGenres() {
        return null;
    }

    @Override
    public List<Genre> getGenresByFilmID(long id) {
        return null;
    }

    @Override
    public void addGenresByFilm(Film film) {

    }

    @Override
    public void updateFilmInStorage(Film film) throws NotFoundException {
        filmCheck(film.getId());
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) throws NotFoundException {
        filmCheck(id);
        return films.get(id);
    }

    @Override
    public Film addLike(long id, long userId) throws NotFoundException {
        filmCheck(id);
        Film film = films.get(id);
        film.getLikesList().add(userId);
        return film;
    }

    @Override
    public Film deleteLike(long id, long userId) throws NotFoundException {
        filmCheck(id);
        Film film = films.get(id);
        film.getLikesList().remove(userId);
        return film;
    }

    @Override
    public List<Long> getLikes(long id) {
        return new ArrayList<>(films.get(id).getLikesList());
    }

    @Override
    public void filmCheck(long id) throws NotFoundException {
        if (films.get(id) == null)
            throw new NotFoundException("Фильм id = " + id + " не найден!");
    }
}
