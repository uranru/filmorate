package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorageDb;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Qualifier("genres")
public class GenreService {

    private final GenreStorageDb genreStorage;

    @Autowired
    public GenreService(GenreStorageDb storage) {
        this.genreStorage = storage;
    }

    public Genre findGenreById(Integer genreId){ return genreStorage.findGenreById(genreId); }

    public List<Genre> findAllGenres(){ return genreStorage.findAllGenres();  }

}
