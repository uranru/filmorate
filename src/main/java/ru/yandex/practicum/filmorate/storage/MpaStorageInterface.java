package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorageInterface {
    Mpa findMpaById(Integer mpaId);

    List<Mpa> findAllMpa();
}
