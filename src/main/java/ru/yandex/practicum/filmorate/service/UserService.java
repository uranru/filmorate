package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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

    public User findObject(Long id) {
        return userStorage.findUserById(id);
    }

    public List<User> findAllObjects() {
        return userStorage.findAllUsers();
    }

    public User addUser(User user) {
        incomingObjectValidation(user);
        return userStorage.addUser(user);
    }

    public User updateObject(User object) { return userStorage.updateUser(object); }
    public void deleteObjectById(Long id) { userStorage.deleteUserById(id); }

    public void addFriend(Long userId, Long friendId){
        userStorage.addFriend(userId,friendId);
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
