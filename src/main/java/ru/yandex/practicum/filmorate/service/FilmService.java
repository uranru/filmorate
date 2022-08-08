package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
@Qualifier("films")
public class FilmService implements UniversalServiceInterface{
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAllObjects() {
        return filmStorage.findAllObjects();
    }

    public Film findObject(Long id) { return (Film) filmStorage.findObject(id); }

    public void createObject(Film object) { filmStorage.createObject(object); }

    public void updateObject(Film object) { filmStorage.updateObject(object); }

    public void addLike(Long filmId, Long userId){
        User user = userStorage.findObject(userId);
        Film film = filmStorage.findObject(filmId);

        Set<Long> listLikes;
        if (film.getListLikes() != null) {
            listLikes = film.getListLikes();
        } else {
            listLikes = new HashSet<>();
        }
        listLikes.add(userId);
        film.setListLikes(listLikes);
        filmStorage.updatePopularFilm();
    }

    public List<Film> findPopularFilms(Long count) {
        List<Film> listFilms = new ArrayList<>();

        if (count == null) {
            count = 10L;
        }
        if (count > filmStorage.getListPopularFilms().size()) {
            count = Long.valueOf(filmStorage.getListPopularFilms().size());
        }

        int i = 0;
        Iterator<Film> iter = filmStorage.getListPopularFilms().iterator();
        while (i < count.intValue() && iter.hasNext()) {
            Film f = iter.next();
            listFilms.add(f);
            i++;
        }

        return listFilms;
    }

    public void deleteLike(Long filmId, Long userId) {
        User user = userStorage.findObject(userId);
        Film film = filmStorage.findObject(filmId);

        Set<Long> listLikes;
        if (film.getListLikes() != null) {
            listLikes = film.getListLikes();

            listLikes.remove(userId);
            film.setListLikes(listLikes);
            filmStorage.updatePopularFilm();
        }
    }
}
