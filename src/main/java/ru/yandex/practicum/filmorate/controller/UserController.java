package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("users")
@Qualifier("users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping("")
    public List<User> findAllObjects() {
        List<User> allObjects = userService.findAllObjects();
        log.info("Запрошен список всех объектов. Текущее количество объектов: {}",allObjects.size());
        return allObjects;
    }

    @GetMapping("/{id}")
    public User findObject(@PathVariable Long id) {
        User object = (User) userService.findUserById(id);
        log.info("Запрошен объект: {}",object);
        return object;
    }

    @PostMapping(value = "")
    public User createObject(@Valid @RequestBody User object) {
        User user = userService.addUser(object);
        log.info("Добавлен новый объект: {}",user);
        return user;
    }

    @PutMapping(value = "")
    public User updateObject(@Valid @RequestBody User user) {
        User userAfterUpdate = userService.updateObject(user);
        log.info("Изменен объект: {}",userAfterUpdate);
        return userAfterUpdate;

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteObjectById(@PathVariable @Valid @RequestBody Long id) {
        userService.deleteObjectById(id);
        log.info("Удален пользователь с ID {}", id);
        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> addFriend(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long friendId) {
        userService.addFriend(id,friendId);
        log.info("Пользователь с ID {} добавлен в друзья пользователю с ID {}",friendId,id);

        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> deleteFriend(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long friendId) {
        userService.deleteFriend(id,friendId);
        log.info("Удален друг с ID {} у пользователя с ID {}",friendId,id);
        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@Valid @PathVariable Long id) {
        List<User> listObject = userService.findFriends(id);
        log.debug("Запрошен список друзей пользователя с ID {}",id);
        return listObject;
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long friendId) {
        List<User> listUser = userService.findCommonFriends(id,friendId);
        log.debug("Запрошен список общих друзей пользователей c ID {} и ID {}",friendId,id);
        return listUser;
    }


}
