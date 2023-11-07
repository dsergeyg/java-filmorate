package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    void addUserToStorage(User user);

    Map<Integer, User> getUserStorage();
}
