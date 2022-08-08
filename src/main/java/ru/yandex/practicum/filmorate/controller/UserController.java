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
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<User> findAllObjects() {
        List<User> allObjects = service.findAllObjects();
        log.info("Запрошен список всех объектов. Текущее количество объектов: {}",allObjects.size());
        return allObjects;
    }

    @GetMapping("/{id}")
    public User findObject(@PathVariable Long id) {
        User object = (User) service.findObject(id);
        log.info("Запрошен объект: {}",object);
        return object;
    }

    @PostMapping(value = "")
    public User createObject(@Valid @RequestBody User object) {
        service.createObject(object);
        log.info("Добавлен новый объект: {}",object);
        return object;
    }

    @PutMapping(value = "")
    public User updateObject(@Valid @RequestBody User object) {
        service.updateObject(object);
        log.info("Изменен объект: {}",object);
        return object;

    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> addFriend(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long friendId) {
        service.addFriend(id,friendId);
        log.info("Пользователь с ID {} добавлен в друзья пользователю с ID {}",friendId,id);

        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> deleteFriend(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long friendId) {
        service.deleteFriend(id,friendId);
        log.info("Удален друг с ID {} у пользователя с ID {}",friendId,id);
        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@Valid @PathVariable Long id) {
        List<User> listObject = service.findFriends(id);
        log.debug("Запрошен список друзей пользователя с ID {}",id);
        return listObject;
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long friendId) {
        List<User> listUser = service.findCommonFriends(id,friendId);
        log.debug("Запрошен список общих друзей пользователей c ID {} и ID {}",friendId,id);
        return listUser;
    }
}
