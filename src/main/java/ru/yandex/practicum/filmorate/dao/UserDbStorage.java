package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUserToStorage(User user) {
        String sqlInsert = "INSERT INTO user_data (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlInsert, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void updateUserInStorage(User user) {
        getUserById(user.getId());
        String sqlUpdate = "UPDATE user_data SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";

        jdbcTemplate.update(sqlUpdate,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Пользователь обновлен: {} {} {}", user.getId(), user.getLogin(), user.getEmail());
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT user_id, email, login, name, birthday  FROM user_data";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(id, email, login, name, birthday, new HashSet<>());

        if (getFriends(id) != null)
            user.getFriendsList().addAll(getFriends(id));
        return user;
    }

    @Override
    public User getUserById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM user_data WHERE user_id = ? LIMIT 1", id);
        if (sqlRowSet.next()) {
            User user = new User(
                    sqlRowSet.getLong("user_id"),
                    sqlRowSet.getString("email"),
                    sqlRowSet.getString("login"),
                    sqlRowSet.getString("name"),
                    sqlRowSet.getDate("birthday").toLocalDate(),
                    new HashSet<>());
            if (getFriends(user.getId()) != null)
                user.getFriendsList().addAll(getFriends(user.getId()));
            log.info("Найден пользователь: {} {} {}", user.getId(), user.getLogin(), user.getEmail());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь id = " + id + " не найден!");
        }
    }

    @Override
    public User addFriend(long id, long friendId) {
        getUserById(id);
        getUserById(friendId);
        //"partial index" are not implemented in H2
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM friendship WHERE user_id = ? AND friend_id = ?", id, friendId);
        if (!sqlRowSet.next()) {
            String sqlInsert = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlInsert, id, friendId);
        }
        log.info("Друг добавлен: {} {}", id, friendId);
        return getUserById(id);
    }

    @Override
    public User deleteFriend(long id, long friendId) {
        getUserById(id);
        getUserById(friendId);

        String sqlDelete = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlDelete, id, friendId);

        log.info("Друг удален: {} {}", id, friendId);
        return getUserById(id);
    }

    @Override
    public List<Long> getFriends(long id) {
        String sql = "SELECT friend_id FROM friendship WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("friend_id"), id);
    }

    @Override
    public User acceptFriend(long id, long friendId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM friendship WHERE user_id = ? AND friend_id = ?", friendId, id);
        if (!sqlRowSet.next()) {
            String sqlInsert = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlInsert, friendId, id);
        }
        return getUserById(id);
    }
}
