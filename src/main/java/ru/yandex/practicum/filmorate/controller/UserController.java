package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    @PostMapping("/api/v1/users/user")
    public User createUser (@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            for(FieldError fieldError : result.getFieldErrors())
                log.error(fieldError.getDefaultMessage());
        }
        log.info("Получен запрос на создание" + user);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @PutMapping("/api/v1/users/user")
    public User updateUser (@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            for(FieldError fieldError : result.getFieldErrors())
                log.error(fieldError.getDefaultMessage());
        }
        int id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            return users.get(id);
        } else {
            log.info(user + "Not found!");
            return user;
        }
    }
    @GetMapping("/api/v1/users")
    public List<User> listUsers() {
        return new ArrayList<>(users.values());
    }
}
