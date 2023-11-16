package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        filmStorage.updateFilmInStorage(film);
        return film;
    }

    public List<Film> getFilms() {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка фильмов");
        return filmStorage.getFilms();
    }

    public Film getFilm(long id) throws NotFoundException {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(long id, long userId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Для фильма: " + id + ", получен запрос на добавление лайка пользователя: " + userId);
        userStorage.userCheck(userId);
        return filmStorage.addLike(id, userId);
    }

    public Film deleteLike(long id, long userId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Для фильма: " + id + ", получен запрос на удаление лайка пользователя: " + userId);
        userStorage.userCheck(userId);
        return filmStorage.deleteLike(id, userId);
    }

    public List<Film> getCountPopularFilms(int count) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка " + count + " самых популярных фильмов");
        return filmStorage
                .getFilms()
                .stream()
                .sorted((Comparator.comparingInt(o -> -o.getLikesList().size())))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Genre getGenre(long id) {
        return filmStorage.getGenreById(id);
    }

    public List<Rating> getRatings() {
        return filmStorage.getRatings();
    }

    public Rating getRating(long id) {
        return filmStorage.getRatingById(id);
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(UtilService.MIN_FILM_DATE))
            throw new ValidationException("Release date may not be before " + UtilService.getOnlyDateStr(UtilService.MIN_FILM_DATE) + " " + film);
    }
}
