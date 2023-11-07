package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private int idSequence;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
        Optional<Integer> maxId = userStorage.getUserStorage().keySet().stream().max(Comparator.naturalOrder());
        this.idSequence = maxId.orElse(0);
    }

    public User userController(User user, boolean isCreate) throws ValidationException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на " + (isCreate ? "создание" : "обновление") + " User " + user);
        if (user.getLogin().contains(" "))
            throw new ValidationException("Login may not contain blanks " + user);
        if (isCreate) {
            user.setId(++idSequence);
            user.setFriendsList(new HashSet<>());
        } else {
            if (!userStorage.getUserStorage().containsKey(user.getId()))
                throw new NotFoundException("Object not found " + user);
            else {
                HashSet<Integer> friendList = userStorage.getUserStorage().get(user.getId()).getFriendsList();
                user.setFriendsList(friendList);
            }
        }
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        userStorage.addUserToStorage(user);
        return user;
    }

    public List<User> getUsers() {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка пользователей");
        return new ArrayList<>(userStorage.getUserStorage().values());
    }

    public User friendsController(String id, String friendId, boolean isAdd) throws NumberFormatException, NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на " + (isAdd ? "добавление" : "удаление") + " пользователя " + friendId + " в друзья пользователю " + id);
        int curId = Integer.parseInt(id);
        int curFriendId = Integer.parseInt(friendId);
        if (userStorage.getUserStorage().containsKey(curId)) {
            if (userStorage.getUserStorage().containsKey(curFriendId)) {
                User user = userStorage.getUserStorage().get(curId);
                User friendUser = userStorage.getUserStorage().get(curFriendId);
                if (isAdd) {
                    user.getFriendsList().add(curFriendId);
                    friendUser.getFriendsList().add(curId);
                } else {
                    user.getFriendsList().remove(curFriendId);
                    friendUser.getFriendsList().remove(curId);
                }
                return user;
            } else
                throw new NotFoundException("Пользователь friendId = " + curFriendId + " не найден!");
        } else
            throw new NotFoundException("Пользователь id = " + curId + " не найден!");
    }

    public List<Integer> getFriendList(String id) throws NumberFormatException, NoSuchElementException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка друзей пользователя с id = " + id);
        int curId = Integer.parseInt(id);
        if (userStorage.getUserStorage().containsKey(curId))
            return new ArrayList<>(userStorage.getUserStorage().get(curId).getFriendsList());
        else
            throw new NoSuchElementException("Пользователь id = " + curId + " не найден!");
    }

    public List<Integer> getCommonFriendList(String id, String otherId) throws NumberFormatException, NotFoundException {
        log.info(UtilService.getDateWithTimeStr(LocalDateTime.now()) + " Получен запрос на получение списка общих друзей пользователя с id = " + id + " и пользователя с id = " + otherId);
        int curId = Integer.parseInt(id);
        int curOtherId = Integer.parseInt(otherId);
        if (userStorage.getUserStorage().containsKey(curId)) {
            if (userStorage.getUserStorage().containsKey(curOtherId)) {
                List<Integer> result = new ArrayList<>(userStorage.getUserStorage().get(curId).getFriendsList());
                result.retainAll(new ArrayList<>(userStorage.getUserStorage().get(curOtherId).getFriendsList()));
                return result;
            } else
                throw new NotFoundException("Пользователь otherId = " + curOtherId + " не найден!");
        } else
            throw new NotFoundException("Пользователь id = " + curId + " не найден!");
    }
}
