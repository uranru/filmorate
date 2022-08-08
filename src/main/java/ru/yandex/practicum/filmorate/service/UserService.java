package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Qualifier("users")
public class UserService implements UniversalServiceInterface{
    private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public List<User> findAllObjects() {
        return storage.findAllObjects();
    }

    public Object findObject(Long id) {
        return storage.findObject(id);
    }

    public void createObject(User object) {
        storage.createObject(object);
    }

    public void updateObject(User object) {
        storage.updateObject(object);
    }

    public void addFriend(Long id, Long friendId){
        User user;
        User friend;
        try {
            user = storage.findObject(id);
            friend = storage.findObject(friendId);

            Set<Long> userListFriends;
            if (user.getListFriends() != null) {
                userListFriends = user.getListFriends();
            } else {
                userListFriends = new HashSet<>();
            }
            userListFriends.add(friendId);
            user.setListFriends(userListFriends);

            Set<Long> friendListFriends;
            if (friend.getListFriends() != null) {
                friendListFriends = friend.getListFriends();
            } else {
                friendListFriends = new HashSet<>();
            }
            friendListFriends.add(id);
            friend.setListFriends(friendListFriends);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "User not Found");
        }
    }

    public List<User> findFriends(Long id){
        List<User> ListCommonFriends = new ArrayList<>();
        User user = storage.findObject(id);
        Set<Long> userListFriends;

        if (user.getListFriends() == null) {
            return ListCommonFriends;
        } else {
            userListFriends = user.getListFriends();
        }

        for (Long idCommonFriend: userListFriends) {
            ListCommonFriends.add(storage.findObject(idCommonFriend));
        }

        return ListCommonFriends;
    }

    public List<User> findCommonFriends(Long id,Long friendId) {
        List<User> ListCommonFriends = new ArrayList<>();
        User user = storage.findObject(id);
        User friend = storage.findObject(friendId);
        Set<Long> userListFriends;
        Set<Long> friendListFriends;

        if (user.getListFriends() == null) {
            return ListCommonFriends;
        } else {
            userListFriends = user.getListFriends();
        }
        if (friend.getListFriends() == null) {
            return ListCommonFriends;
        } else {
            friendListFriends = friend.getListFriends();
        }

        for (Long idCommonFriend: userListFriends) {
            if (friendListFriends.contains(idCommonFriend)) {
                ListCommonFriends.add(storage.findObject(idCommonFriend));
            }
        }

        return ListCommonFriends;
    }

    public void deleteFriend(Long id, Long friendId){
        List<User> ListCommonFriends = new ArrayList<>();
        User user = storage.findObject(id);
        User friend = storage.findObject(friendId);

        if (user.getListFriends() != null)  {
            user.getListFriends().remove(friendId);
        }
        if (friend.getListFriends() != null) {
            friend.getListFriends().remove(id);
        }
    }

}
