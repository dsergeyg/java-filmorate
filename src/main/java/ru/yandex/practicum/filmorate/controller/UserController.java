package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
public class UserController {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    private final Map<Integer, User> users = new HashMap<>();
    private int idSequence = 0;

    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws ValidationException {
        log.info(formatter.format(LocalDateTime.now()) +  "Получен запрос на создание User " + user);
        try {
            return userController(user, true);
        } catch (ValidationException | javax.validation.ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException("Bad request");
        }
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException {
        log.info(formatter.format(LocalDateTime.now()) +  "Получен запрос на обновление User " + user);
        try {
            return userController(user, false);
        } catch (ValidationException | javax.validation.ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException("Bad request");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private User userController(User user, boolean isCreate) throws ValidationException {
        Optional<ConstraintViolation<User>> violation = validator.validate(user).stream().findFirst();
        if (violation.isPresent()) {
            throw new ValidationException(violation.get().getMessage());
        }
        if (user.getLogin().contains(" "))
            throw new ValidationException("Login may not contain blanks " + user);
        if (isCreate) {
            user.setId(++idSequence);
        }
        else {
            if (!users.containsKey(user.getId())) {
                throw new ValidationException("Object not found " + user);
            }
        }
        String curName;
        if (user.getName() != null)
            curName = user.getName().isBlank() ? user.getLogin() : user.getName();
        else
            curName = user.getLogin();
        user.setName(curName);
        users.put(user.getId(), user);
        return user;
    }
}
