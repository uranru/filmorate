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


import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("films")
@Qualifier("films")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService service) {
        this.filmService = service;
    }

    @GetMapping("")
    public List<Film> findAllFilms() {
        List<Film> allObjects = filmService.findAllFilms();
        log.info("Запрошен список всех объектов. Текущее количество объектов: {}",allObjects.size());
        return allObjects;
    }

    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable Long filmId) {
        Film film = filmService.findFilmById(filmId);
        log.info("Запрошен фильм: {}",film);
        return film;
    }

    @PostMapping(value = "")
    public Film addFilm(@Valid @RequestBody Film film) {
        Film newFilm = filmService.addFilm(film);
        log.info("Добавлен новый объект: {}",newFilm);
        return newFilm;
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<User> deleteFilmById(@PathVariable @Valid @RequestBody Long filmId) {
        filmService.deleteFilmById(filmId);
        log.info("Удален пользователь с ID {}", filmId);
        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @PutMapping(value = "")
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film filmReturn = filmService.updateFilm(film);
        log.info("Изменен объект: {}",filmReturn);
        return filmReturn;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Object> addLike(@PathVariable @Valid @RequestBody Long filmId, @PathVariable @Valid @RequestBody Long userId) {
        filmService.addLike(filmId,userId);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}",userId, filmId);

        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Object> deleteLike(@PathVariable @Valid @RequestBody Long id, @PathVariable @Valid @RequestBody Long userId) {
        filmService.deleteLike(id,userId);
        log.info("Пользователь с ID {} удалил лайк фильму с ID {}",userId,id);
        return new ResponseEntity<>(HttpStatus.resolve(200));
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10") String count) {
        log.debug("Запрошен список {} наиболее популярных фильмов",count);
        List<Film> listFilms = filmService.findPopularFilms(Integer.valueOf(count));
        return listFilms;
    }


}

