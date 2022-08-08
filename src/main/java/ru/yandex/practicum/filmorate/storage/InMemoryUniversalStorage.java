package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class InMemoryUniversalStorage<Object> implements UniversalStorage<Object> {
    private Long i = 0L;
    private static final Logger log = LoggerFactory.getLogger(UniversalStorage.class);
    protected final Map<Long,Object> listObjects = new HashMap<>();

    @Override
    public List<Object> findAllObjects() {
        return viewListObjects();
    }

    @Override
    public Object findObject(Long id) {
        Object object = listObjects.get(id);
        if (object != null) {
            return object;
        } else {
           throw new ResponseStatusException(
                HttpStatus.resolve(404), "Object not Found");
        }
    }

    @Override
    public Object createObject(Object newObject) {
        Long id = generateId();
        setId(newObject,id);
        checkObject(newObject);
        listObjects.put(id,newObject);
        return newObject;
    }

    @Override
    public Object updateObject(Object object) {
        Long id = getId(object);
        if (listObjects.containsKey(id)) {
            listObjects.put(id,object);
            checkObject(object);
            log.info("Обновлен объект: {}",object);
            return object;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "Object not Found");
        }
    }

    @Override
    public Long generateId() {
        i ++;
        return i;
    }

    @Override
    public List<Object> viewListObjects() {
        List<Object> listView = new ArrayList<>();
        for (Object object : listObjects.values()) {
            listView.add(object);
        }
        return listView;
    }

    abstract void setId (Object object,Long id);

    abstract void checkObject (Object object);

    abstract Long getId (Object object);
}
