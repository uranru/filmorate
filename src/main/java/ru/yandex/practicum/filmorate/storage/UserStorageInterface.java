package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorageInterface {

    List<User> findAllUsers();

    User findUserById(Long id);

    User findUserByLogin(String userLogin);

    User addUser(User newUser);

    User updateUser(User object);

    void deleteUserById(Long userId);

    void addFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);

    List<User> findCommonFriends(Long userId,Long friendId);

    void deleteFriend(Long userId, Long friendId);
    //Long generateId();

    public List<User> viewListObjects();
}
