package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long idSequence;

    @Override
    public void addUserToStorage(User user) {
        user.setId(++idSequence);
        users.put(user.getId(), user);
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
        return users.get(id);
    }
}
