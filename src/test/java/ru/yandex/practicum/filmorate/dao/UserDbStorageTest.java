package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        userStorage.addUserToStorage(newUser);

        User savedUser = userStorage.getUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUserInStorage() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        userStorage.addUserToStorage(newUser);
        User updUser = new User(1L, "new_user@email.ru", "new_login", "Ivan Petrov", LocalDate.of(1995, 1, 1), new HashSet<>());
        userStorage.updateUserInStorage(updUser);

        User savedUser = userStorage.getUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updUser);
    }

    @Test
    public void testGetUsers() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        User secondNewUser = new User(2L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);

        List<User> userList = new ArrayList<>();

        userList.add(newUser);
        userList.add(secondNewUser);

        assertEquals(userList, userStorage.getUsers());
    }

    @Test
    public void testAddFriend() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        User secondNewUser = new User(2L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);

        userStorage.addFriend(1, 2);
        List<Long> friendListFistUser = userStorage.getFriends(1);
        assertTrue(friendListFistUser.contains(2L));
        List<Long> friendListSecondUser = userStorage.getFriends(2);
        assertTrue(friendListSecondUser.contains(1L));
    }

    @Test
    public void testDeleteFriend() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        User secondNewUser = new User(2L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);
        userStorage.addFriend(1, 2);

        List<Long> friendListFistUser = userStorage.getFriends(1);
        assertTrue(friendListFistUser.contains(2L));

        List<Long> friendListSecondUser = userStorage.getFriends(2);
        assertTrue(friendListSecondUser.contains(1L));

        userStorage.deleteFriend(1, 2);
        assertTrue(userStorage.getFriends(1).isEmpty());
        assertTrue(userStorage.getFriends(2).isEmpty());
    }

    @Test
    public void testGetFriends() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());
        User secondNewUser = new User(2L, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1), new HashSet<>());

        userStorage.addUserToStorage(newUser);
        userStorage.addUserToStorage(secondNewUser);
        userStorage.addFriend(1, 2);

        List<Long> friendListFistUser = userStorage.getFriends(1);
        assertTrue(friendListFistUser.contains(2L));

        List<Long> friendListSecondUser = userStorage.getFriends(2);
        assertTrue(friendListSecondUser.contains(1L));
    }
}
