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
        return userService.userController(user, true);
    }

    @PutMapping("/users")
    public User putUser(@Validated(Update.class) @RequestBody User user) {
        return userService.userController(user, false);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User putFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.friendsController(id, friendId, true);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.friendsController(id, friendId, false);
    }

    @GetMapping("/users/{id}/friends")
    public List<Integer> getFriends(@PathVariable String id) {
        return userService.getFriendList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<Integer> getCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        return userService.getCommonFriendList(id, otherId);
    }

}
