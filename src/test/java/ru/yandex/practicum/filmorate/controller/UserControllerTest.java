package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private User user;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = User.builder().setLogin("SomeLogin")
                .setName("SomeName")
                .setEmail("my@yandex.ru")
                .setBirthday(LocalDate.of(1985,10,22))
                .build();
    }

    @Test
    void postUser() {
        assertDoesNotThrow(() -> userController.postUser(user));
        user.setName(null);
        assertEquals(userController.postUser(user).getName(), user.getLogin());
        user.setName(" ");
        assertEquals(userController.postUser(user).getName(), user.getLogin());
        user.setEmail("SomeEmail");
        assertThrows(ValidationException.class, () -> userController.postUser(user));
        user.setEmail("my@yandex.ru");
        user.setBirthday(LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.postUser(user));
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.postUser(user));
        user.setBirthday(LocalDate.now().minusDays(1));
        assertDoesNotThrow(() -> userController.postUser(user));
        assertTrue(userController.getUsers().contains(user));
    }

    @Test
    void putUser() {
        User curUser = userController.postUser(user);

        assertTrue(userController.getUsers().contains(curUser));
        assertDoesNotThrow(() -> userController.postUser(curUser));
        curUser.setName(null);
        assertEquals(userController.putUser(curUser).getName(), curUser.getLogin());
        curUser.setName(" ");
        assertEquals(userController.putUser(curUser).getName(), curUser.getLogin());
        curUser.setEmail("SomeEmail");
        assertThrows(ValidationException.class, () -> userController.putUser(curUser));
        curUser.setEmail("my@yandex.ru");
        curUser.setBirthday(LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.putUser(curUser));
        curUser.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.putUser(curUser));
        curUser.setBirthday(LocalDate.now().minusDays(1));
        assertDoesNotThrow(() -> userController.putUser(curUser));
        assertTrue(userController.getUsers().contains(curUser));
    }

    @Test
    void getUsers() {
        User curUser = User.builder().setLogin("SomeLogin1")
                .setName("SomeName1")
                .setEmail("my@yandex.ru")
                .setBirthday(LocalDate.now().minusDays(1))
                .build();

        List<User> listUser = new ArrayList<>();
        listUser.add(userController.postUser(user));
        listUser.add(userController.postUser(curUser));
        assertEquals(listUser, userController.getUsers());
        curUser.setName("SomeFilm2");
        curUser.setEmail("someEmail@yandex.ru");
        curUser.setBirthday(LocalDate.now().minusDays(10));
        userController.putUser(curUser);
        assertEquals(listUser, userController.getUsers());
    }
}