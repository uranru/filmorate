package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dao.UserStorageDb;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Qualifier("users")
public class UserService {
    private final UserStorageDb userStorage;

    @Autowired
    public UserService(UserStorageDb storage) {
        this.userStorage = storage;
    }

    public User findUserById(Long id) {
        try {
            return userStorage.findUserById(id);
        } catch (Exception exception) {
            throw new ResponseStatusException(
                 HttpStatus.resolve(404), "");
        }
    }

    public List<User> findAllObjects() {
        return userStorage.findAllUsers();
    }

    public User addUser(User user) {
        incomingObjectValidation(user);
        if (userStorage.addUser(user) > 0) {
            return userStorage.findUserByLogin(user.getLogin());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.resolve(400), "");
        }
    }

    public User updateObject(User user) {
        if (userStorage.updateUser(user) > 0) {
            return userStorage.findUserByLogin(user.getLogin());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "");
        }
    }
    public void deleteObjectById(Long id) {
        if (userStorage.deleteUserById(id) == 0) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(400), "");
        }
    }

    public void addFriend(Long userId, Long friendId){
        try {
            userStorage.addFriend(userId,friendId);
        }   catch (Exception exception) {
                throw new ResponseStatusException(
                    HttpStatus.resolve(404), "");
        }
    }

    public List<User> findFriends(Long userId){
        return userStorage.findFriends(userId);
    }

    public List<User> findCommonFriends(Long userId, Long friendId) { return userStorage.findCommonFriends(userId,friendId);}

    public void deleteFriend(Long userId, Long friendId){ userStorage.deleteFriend(userId, friendId); }

    private void incomingObjectValidation(User user){
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
