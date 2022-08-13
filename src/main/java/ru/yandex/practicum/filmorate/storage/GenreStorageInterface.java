package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface GenreStorageInterface {

    Genre findGenreById(Integer genreId);

    List<Genre> findAllGenres();
}
