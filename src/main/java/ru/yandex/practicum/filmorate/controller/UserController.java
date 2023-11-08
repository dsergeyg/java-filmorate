package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.*;
import java.util.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User putUser(@Validated(Update.class) @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User putFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriendList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriendList(id, otherId);
    }

}
