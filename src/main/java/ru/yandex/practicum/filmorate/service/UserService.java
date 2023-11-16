package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
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
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на создание User: " + user);
        userValidate(user);
        userStorage.addUserToStorage(user);
        return user;
    }

    public User updateUser(User user) throws ValidationException {
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

    public User getUser(long id) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение пользователя");
        return userStorage.getUserById(id);
    }

    public User addFriends(long id, long friendId) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на добавление пользователя " + friendId + " в друзья пользователю " + id);
        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriends(long id, long friendId) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на удаление пользователя " + friendId + " из друзей пользователя " + id);
        return userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriendList(long id) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка друзей пользователя с id = " + id);
        userStorage.getUserById(id);
        List<Long> friends = userStorage.getFriends(id);
        return friends
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendList(long id, long otherId) {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка общих друзей пользователя с id = " + id + " и пользователя с id = " + otherId);
        userStorage.getUserById(id);
        userStorage.getUserById(otherId);
        List<Long> friends = userStorage.getFriends(id);
        List<Long> otherFriends = userStorage.getFriends(otherId);
        return friends
                .stream()
                .filter(otherFriends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private void userValidate(User user) throws ValidationException {
        if (user.getLogin().contains(" "))
            throw new ValidationException("Login may not contain blanks " + user);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
