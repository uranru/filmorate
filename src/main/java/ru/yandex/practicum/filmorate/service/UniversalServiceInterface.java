package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.InMemoryUniversalStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

public interface UniversalServiceInterface {
    @Autowired
    public InMemoryUniversalStorage storage = new InMemoryUserStorage();

    default  <Object> List<Object> findAllObjects() {
            return storage.findAllObjects();
        }

    default Object findObject(Long id) {
            return storage.findObject(id);
        }

    default <Object> void createObject(Object object) {
            storage.createObject(object);
        }

    default <Object> void updateObject(Object object) {
            storage.updateObject(object);
        }
}
