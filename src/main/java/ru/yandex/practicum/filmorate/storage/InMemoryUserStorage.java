package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long idSequence;

    @Override
    public void addUserToStorage(User user) {
        user.setId(++idSequence);
        users.put(user.getId(), user);
    }

    @Override
    public User acceptFriend(long id, long friendId) {
        return null;
    }

    @Override
    public void updateUserInStorage(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        userCheck(id);
        return users.get(id);
    }

    @Override
    public User addFriend(long id, long friendId) {
        userCheck(id);
        userCheck(friendId);
        User user = users.get(id);
        User friend = users.get(friendId);
        user.getFriendsList().add(friendId);
        friend.getFriendsList().add(id);
        return user;
    }

    @Override
    public User deleteFriend(long id, long friendId) {
        userCheck(id);
        userCheck(friendId);
        User user = users.get(id);
        user.getFriendsList().remove(friendId);
        return user;
    }

    @Override
    public List<Long> getFriends(long id) {
        userCheck(id);
        return new ArrayList<>(users.get(id).getFriendsList());
    }

    public void userCheck(long id) {
        if (users.get(id) == null)
            throw new NotFoundException("Пользователь id = " + id + " не найден!");
    }
}
