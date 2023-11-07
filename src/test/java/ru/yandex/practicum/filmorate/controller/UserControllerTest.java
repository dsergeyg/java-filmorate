package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private User user;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
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
        assertFalse(validator.validate(user).stream().findFirst().isEmpty());
        user.setEmail("my@yandex.ru");
        user.setBirthday(LocalDate.now());
        assertFalse(validator.validate(user).stream().findFirst().isEmpty());
        user.setBirthday(LocalDate.now().plusDays(1));
        assertFalse(validator.validate(user).stream().findFirst().isEmpty());
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
        assertFalse(validator.validate(curUser).stream().findFirst().isEmpty());
        curUser.setEmail("my@yandex.ru");
        curUser.setBirthday(LocalDate.now());
        assertFalse(validator.validate(curUser).stream().findFirst().isEmpty());
        curUser.setBirthday(LocalDate.now().plusDays(1));
        assertFalse(validator.validate(curUser).stream().findFirst().isEmpty());
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