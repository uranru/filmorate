package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorageDb;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@Qualifier("films")
public class FilmService {

    private final FilmStorageDb filmStorage;

    @Autowired
    public FilmService(FilmStorageDb storage) {
        this.filmStorage = storage;
    }

    public Film addFilm(Film film) {
        Film newFilm = filmStorage.addFilm(film);
        return newFilm;
    }

    public Film findFilmById(Long filmId){ return filmStorage.findFilmById(filmId); }

    public List<Film> findAllFilms() { return filmStorage.findAllFilms(); }

    public List<Film> findPopularFilms(Integer filmsCount){ return filmStorage.findPopularFilms(filmsCount); }

    public void deleteFilmById(Long filmId){ filmStorage.deleteFilmById(filmId); }

    public Film updateFilm(Film film) { return filmStorage.updateFilm(film); }

    public void addLike(Long filmId, Long userId) { filmStorage.addLike(filmId,userId); }

    public void deleteLike(Long filmId, Long userId) { filmStorage.deleteLike(filmId,userId); }
}
