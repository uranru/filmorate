package ru.yandex.practicum.filmorate.controller;

//import org.intellij.lang.annotations.JdkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUniversalStorage;


import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("films")
@Qualifier("films")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<Film> findAllObjects() {
        List<Film> allObjects = service.findAllObjects();
        log.info("Запрошен список всех объектов. Текущее количество объектов: {}",allObjects.size());
        return allObjects;
    }

    @GetMapping("/{id}")
    public Film findObject(@PathVariable Long id) {
        Film object = (Film) service.findObject(id);
        log.info("Запрошен объект: {}",object);
        return object;
    }

    @PostMapping(value = "")
    public Film createObject(@Valid @RequestBody Film object) {
        service.createObject(object);
        log.info("Добавлен новый объект: {}",object);
        return object;
    }

    @PutMapping(value = "")
    public Film updateObject(@Valid @RequestBody Film object) {
        service.updateObject(object);
        log.info("Изменен объект: {}",object);
        return object;
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Object> addLike(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long userId) {
        service.addLike(id,userId);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}",userId,id);

        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Object> deleteLike(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long userId) {
        service.deleteLike(id,userId);
        log.info("Пользователь с ID {} удалил лайк фильму с ID {}",userId,id);
        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false) Long count) {
        List<Film> listFilms = service.findPopularFilms(count);
        log.debug("Запрошен список {} наиболее популярных фильмов",count);
        return listFilms;
    }
}

