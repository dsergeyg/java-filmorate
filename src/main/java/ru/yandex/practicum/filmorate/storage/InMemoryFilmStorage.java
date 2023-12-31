package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public void updateFilmInStorage(Film film) {
        filmCheck(film.getId());
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) {
        filmCheck(id);
        return films.get(id);
    }

    @Override
    public Film addLike(long id, long userId) {
        filmCheck(id);
        Film film = films.get(id);
        film.getLikesList().add(userId);
        return film;
    }

    @Override
    public Film deleteLike(long id, long userId) {
        filmCheck(id);
        Film film = films.get(id);
        film.getLikesList().remove(userId);
        return film;
    }

    @Override
    public List<Long> getLikes(long id) {
        return new ArrayList<>(films.get(id).getLikesList());
    }

    public void filmCheck(long id) {
        if (films.get(id) == null)
            throw new NotFoundException("Фильм id = " + id + " не найден!");
    }
}
