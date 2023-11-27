package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
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
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       GenreStorage genreStorage,
                       RatingStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на создание Film: " + film);
        filmValidation(film);
        filmStorage.addFilmToStorage(film);
        film.setMpa(ratingStorage.getRatingById(film.getMpa().getId()));
        if (!film.getGenres().isEmpty())
            genreStorage.addGenresByFilm(film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на обновление Film: " + film);
        filmValidation(film);
        filmStorage.updateFilmInStorage(film);
        if (!film.getGenres().isEmpty())
            genreStorage.addGenresByFilm(film);
        else
            genreStorage.deleteGenresByFilm(film);
        film.setMpa(ratingStorage.getRatingById(film.getMpa().getId()));
        return film;
    }

    public List<Film> getFilms() {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка фильмов");
        List<Film> filmList = filmStorage.getFilms();

        for (Film film : filmList) {
            film.setGenres(new HashSet<>(genreStorage.getGenresByFilmID(film.getId())));
        }
        return filmList;
    }

    public Film getFilm(long id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(new HashSet<>(genreStorage.getGenresByFilmID(film.getId())));
        return film;
    }

    public Film addLike(long id, long userId) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Для фильма: " + id + ", получен запрос на добавление лайка пользователя: " + userId);
        userStorage.getUserById(userId);
        return filmStorage.addLike(id, userId);
    }

    public Film deleteLike(long id, long userId) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Для фильма: " + id + ", получен запрос на удаление лайка пользователя: " + userId);
        userStorage.getUserById(userId);
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

    private void filmValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(UtilService.MIN_FILM_DATE))
            throw new ValidationException("Release date may not be before " + UtilService.getOnlyDateStr(UtilService.MIN_FILM_DATE) + " " + film);
    }
}
