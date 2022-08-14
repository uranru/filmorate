package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorageInterface {

    List<Film> findAllFilms();

    Integer addFilm(Film newFilm);

    Film findFilmByName(String filmName);

    Film findFilmById(Long filmId);

    List<Film> findPopularFilms(Integer countFilms);

    Integer deleteFilmById(Long filmId);

    Integer updateFilm(Film film);

    void addLike(Long filmId,Long userId);

    void deleteLike(Long filmId,Long userId);
}
