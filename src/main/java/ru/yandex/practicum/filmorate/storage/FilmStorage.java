package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Map;

public interface FilmStorage {

    void addFilmToStorage(Film film);

    Map<Integer, Film> getFilmStorage();
}
