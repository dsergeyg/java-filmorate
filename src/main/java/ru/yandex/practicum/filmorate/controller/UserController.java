package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    private final Map<Integer, User> users = new HashMap<>();
    private int idSequence = 0;

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) throws ValidationException {
        log.info(formatter.format(LocalDateTime.now()) +  "Получен запрос на создание User " + user);
        try {
            return userController(user, true);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException("Bad request");
        }
    }

    @PutMapping("/users")
    public User putUser(@Validated(Update.class) @RequestBody User user) throws ValidationException {
        log.info(formatter.format(LocalDateTime.now()) +  "Получен запрос на обновление User " + user);
        try {
            return userController(user, false);
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException("Bad request");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private User userController(User user, boolean isCreate) throws ValidationException {
        if (user.getLogin().contains(" "))
            throw new ValidationException("Login may not contain blanks " + user);
        if (isCreate) {
            user.setId(++idSequence);
        } else {
            if (!users.containsKey(user.getId())) {
                throw new ValidationException("Object not found " + user);
            }
        }
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }
}
