package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void addUserToStorage(User user);

    void updateUserInStorage(User user);

    List<User> getUsers();

    User getUserById(long id);

    User addFriend(long id, long friendId);

    User deleteFriend(long id, long friendId);

    List<Long> getFriends(long id);

    void userCheck(long id);
}
