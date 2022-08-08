package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("users")
public class InMemoryUserStorage extends InMemoryUniversalStorage<User>{
    protected final Long i = 0L;
    protected final Map<Long,Object> listObjects = new HashMap<>();

    @Override
    protected void setId(User user, Long id) {
        user.setId(id);
    }

    @Override
    protected Long getId(User user) {
        return user.getId();
    }

    @Override
    protected void checkObject (User user){
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
