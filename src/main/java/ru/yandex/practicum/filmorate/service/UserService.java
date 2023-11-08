package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на создание User: " + user);
        userValidate(user);
        userStorage.addUserToStorage(user);
        return user;
    }

    public User updateUser(User user) throws ValidationException, NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на обновление User " + user);
        if (userStorage.getUserById(user.getId()) == null)
            throw new NotFoundException("Object not found " + user);
        userValidate(user);
        userStorage.updateUserInStorage(user);
        return user;
    }

    public List<User> getUsers() {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка пользователей");
        return userStorage.getUsers();
    }

    public User getUser(long id) throws NotFoundException {
        userDataCheck(id);
        return userStorage.getUserById(id);
    }

    public User addFriends(long id, long friendId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на добавление пользователя " + friendId + " в друзья пользователю " + id);
        friendDataCheck(id, friendId);
        User user = userStorage.getUserById(id);
        User friendUser = userStorage.getUserById(friendId);
        user.getFriendsList().add(friendId);
        friendUser.getFriendsList().add(id);
        return user;
    }

    public User deleteFriends(long id, long friendId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на удаление пользователя " + friendId + " из друзей пользователя " + id);
        friendDataCheck(id, friendId);
        User user = userStorage.getUserById(id);
        User friendUser = userStorage.getUserById(friendId);
        user.getFriendsList().remove(friendId);
        friendUser.getFriendsList().remove(id);
        return user;
    }

    public List<User> getFriendList(long id) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка друзей пользователя с id = " + id);
        userDataCheck(id);
        Set<Long> friends = userStorage.getUserById(id).getFriendsList();
        return friends
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendList(long id, long otherId) throws NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка общих друзей пользователя с id = " + id + " и пользователя с id = " + otherId);
        friendDataCheck(id, otherId);
        Set<Long> friends = userStorage.getUserById(id).getFriendsList();
        Set<Long> otherFriends = userStorage.getUserById(otherId).getFriendsList();
        return friends
                .stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private void friendDataCheck(long id, long secondId) throws NotFoundException {
        userDataCheck(id);
        if (userStorage.getUserById(secondId) == null)
            throw new NotFoundException("Пользователь secondId = " + secondId + " не найден!");
    }

    private void userDataCheck(long id) throws NotFoundException {
        if (userStorage.getUserById(id) == null)
            throw new NotFoundException("Пользователь id = " + id + " не найден!");
    }

    private void userValidate(User user) throws ValidationException {
        if (user.getLogin().contains(" "))
            throw new ValidationException("Login may not contain blanks " + user);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        user.setFriendsList(new HashSet<>());
    }
}
