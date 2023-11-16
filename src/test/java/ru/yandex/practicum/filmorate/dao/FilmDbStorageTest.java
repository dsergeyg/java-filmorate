package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    public void testGetFilmById() {
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.addFilmToStorage(newFilm);

        Film savedFilm = filmStorage.getFilmById(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    @DirtiesContext
    public void testUpdateFilmInStorage() {
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.addFilmToStorage(newFilm);

        Film updFilm = new Film(1L, "Some new Name", "Some new Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        filmStorage.updateFilmInStorage(updFilm);

        Film savedFilm = filmStorage.getFilmById(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updFilm);
    }

    @Test
    @DirtiesContext
    public void testGetFilms() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        Film secondNewFilm = new Film(2L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());

        filmStorage.addFilmToStorage(newFilm);
        filmStorage.addFilmToStorage(secondNewFilm);

        List<Film> filmList = new ArrayList<>();

        filmList.add(newFilm);
        filmList.add(secondNewFilm);

        assertEquals(filmList, filmStorage.getFilms());
    }

    @Test
    @DirtiesContext
    public void testAddLike() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        User secondNewUser = new User(2L, "user1@email.ru", "vanya1234", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        filmStorage.addFilmToStorage(newFilm);
        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);

        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        assertThat(filmStorage.getLikes(1)).isNotNull().contains(1L).contains(2L);
    }

    @Test
    @DirtiesContext
    public void testDeleteLike() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1),  new HashSet<>());
        User secondNewUser = new User(2L, "user1@email.ru", "vanya1234", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        filmStorage.addFilmToStorage(newFilm);
        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);

        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        assertThat(filmStorage.getLikes(1)).isNotNull().contains(1L).contains(2L);

        filmStorage.deleteLike(1, 2);
        assertThat(filmStorage.getLikes(1)).isNotNull().contains(1L).doesNotContain(2L);
    }

    @Test
    @DirtiesContext
    public void testGetLikes() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        Film newFilm = new Film(1L, "Some Name", "Some Description", LocalDate.of(1990, 1, 1), 160, new Rating(1, "G"), new HashSet<>(), new HashSet<>());
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        User secondNewUser = new User(2L, "user1@email.ru", "vanya1234", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        filmStorage.addFilmToStorage(newFilm);
        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);

        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        assertThat(filmStorage.getLikes(1)).isNotNull().contains(1L).contains(2L);

        List<Long> filmLikes = new ArrayList<>();
        filmLikes.add(1L);
        filmLikes.add(2L);

        assertEquals(filmLikes, filmStorage.getLikes(1));
    }
}
