package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface UniversalStorage<Object> {

    List<Object> findAllObjects();

    Object findObject(Long id);

    Object createObject(Object newObject);

    Object updateObject(Object object);

    Long generateId();

    public List<Object> viewListObjects();
}
