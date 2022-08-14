package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("mpa")
@Qualifier("mpa")
public class MpaController {
    private final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService service) {
        this.mpaService = service;
    }

    @GetMapping("/{mpaId}")
    public Mpa findMpaById(@PathVariable Integer mpaId) {
        Mpa mpa = mpaService.findMpaById(mpaId);
        log.info("Запрошен MPA: {}",mpa);
        return mpa;
    }

    @GetMapping("")
    public List<Mpa> findAllMpa() {
        List<Mpa> allMpa = mpaService.findAllMpa();
        log.info("Запрошен список всех MPA. Текущее количество объектов: {}",allMpa.size());
        return allMpa;
    }

}
