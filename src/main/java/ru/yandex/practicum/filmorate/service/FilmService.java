package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
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
    private int idSequence;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        Optional<Integer> maxId = filmStorage.getFilmStorage().keySet().stream().max(Comparator.naturalOrder());
        this.idSequence = maxId.orElse(0);
    }

    public Film filmController(Film film, boolean isCreate) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на " + (isCreate ? "создание" : "обновление") + " Film: " + film);
        if (film.getReleaseDate().isBefore(UtilService.MIN_FILM_DATE))
            throw new ValidationException("Release date may not be before " + UtilService.getOnlyDateStr(UtilService.MIN_FILM_DATE) + " " + film);
        if (isCreate) {
            film.setId(++idSequence);
            film.setLikesList(new HashSet<>());
        } else {
            if (!filmStorage.getFilmStorage().containsKey(film.getId()))
                throw new NotFoundException("Object not found " + film);
            else {
                HashSet<Integer> likesList = filmStorage.getFilmStorage().get(film.getId()).getLikesList();
                film.setLikesList(likesList);
            }
        }
        filmStorage.addFilmToStorage(film);
        return film;
    }

    public List<Film> getFilms() {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка фильмов");
        return new ArrayList<>(filmStorage.getFilmStorage().values());
    }

    public Film likeController(String id, String userId, boolean isAdd) throws NumberFormatException, NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) +  " Для фильма: " + id + ", получен запрос на " + (isAdd ? "добавление" : "удаление") + " лайка пользователя: " + userId);
        int curId = Integer.parseInt(id);
        int curUserId = Integer.parseInt(userId);
        if (filmStorage.getFilmStorage().containsKey(curId)) {
            Film film = filmStorage.getFilmStorage().get(curId);
            if (isAdd)
                film.getLikesList().add(curUserId);
            else {
                if (film.getLikesList().contains(curUserId))
                    film.getLikesList().remove(curUserId);
                else
                    throw new NotFoundException("Лайк пользователя id = " + userId + " не найден или уже удален!");
            }
            return film;
        } else
            throw new NotFoundException("Фильм id = " + curId + " не найден!");
    }

    public List<Film> getCountPopularFilms(String count) throws NumberFormatException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка " + count + " самых популярных фильмов");
        int curCount = Integer.parseInt(count);
        if (filmStorage.getFilmStorage().size() < curCount)
            curCount = filmStorage.getFilmStorage().size();
        return new ArrayList<>(filmStorage
                .getFilmStorage()
                .values()
                .stream()
                .sorted((Comparator.comparingInt(o -> -o.getLikesList().size())))
                .collect(Collectors.toList()))
                .subList(0, curCount);
    }
}
