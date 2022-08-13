package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("genres")
@Qualifier("genres")
public class GenreController {
    private final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService service) {
        this.genreService = service;
    }

    @GetMapping("/{genreId}")
    public Genre findGenreById(@PathVariable Integer genreId) {
        Genre genre = genreService.findGenreById(genreId);
        log.info("Запрошен MPA: {}",genre);
        return genre;
    }

    @GetMapping("")
    public List<Genre> findAllGenres() {
        List<Genre> allGenres = genreService.findAllGenres();
        log.info("Запрошен список всех Genres. Текущее количество объектов: {}",allGenres.size());
        return allGenres;
    }
}
