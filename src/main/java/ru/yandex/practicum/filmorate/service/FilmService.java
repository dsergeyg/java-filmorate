package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на создание Film: " + film);
        filmValidation(film);
        filmStorage.addFilmToStorage(film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на обновление Film: " + film);
        filmValidation(film);
        if (filmStorage.getFilmById(film.getId()) == null)
            throw new NotFoundException("Object not found " + film);
        filmStorage.updateFilmInStorage(film);
        return film;
    }

    public List<Film> getFilms() {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка фильмов");
        return filmStorage.getFilms();
    }

    public Film getFilm(long id) throws NotFoundException {
        filmCheck(id);
        return filmStorage.getFilmById(id);
    }

    public Film addLike(long id, long userId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Для фильма: " + id + ", получен запрос на добавление лайка пользователя: " + userId);
        filmCheck(id);
        Film film = filmStorage.getFilmById(id);
        film.getLikesList().add(userId);
        return film;
    }

    public Film deleteLike(long id, long userId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Для фильма: " + id + ", получен запрос на удаление лайка пользователя: " + userId);
        filmCheck(id);
        Film film = filmStorage.getFilmById(id);
        film.getLikesList().remove(userId);
        return film;
    }

    public List<Film> getCountPopularFilms(int count) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка " + count + " самых популярных фильмов");
        return filmStorage
                .getFilms()
                .stream()
                .limit(count)
                .sorted((Comparator.comparingInt(o -> -o.getLikesList().size())))
                .collect(Collectors.toList());
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(UtilService.MIN_FILM_DATE))
            throw new ValidationException("Release date may not be before " + UtilService.getOnlyDateStr(UtilService.MIN_FILM_DATE) + " " + film);
        film.setLikesList(new HashSet<>());
    }

    private void filmCheck(long id) throws NotFoundException {
        if (filmStorage.getFilmById(id) == null)
            throw new NotFoundException("Фильм id = " + id + " не найден!");
    }
}
