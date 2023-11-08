package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    void addUserToStorage(User user);

    void updateUserInStorage(User user);

    List<User> getUsers();

    User getUserById(long id);
}
