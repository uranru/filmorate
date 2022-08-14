package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreStorageInterface {

    Genre findGenreById(Integer genreId);

    List<Genre> findAllGenres();

    List<Map> findAllFilmsAndGenres();

    List<Genre> findGenreByFilmId(Long filmId);
    Integer updateFilmGenre(Long filmId, Integer genreId);

    Integer deleteFilmAllGenres(Long filmId);

}
