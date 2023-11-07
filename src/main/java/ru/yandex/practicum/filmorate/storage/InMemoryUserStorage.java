package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUserToStorage(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Map<Integer, User> getUserStorage() {
        return users;
    }
}
