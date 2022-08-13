package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorageDb;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Qualifier("mpa")
public class MpaService {
    private final MpaStorageDb mpaStorage;

    @Autowired
    public MpaService(MpaStorageDb storage) {
        this.mpaStorage = storage;
    }

    public Mpa findMpaById(Integer mpaId){ return mpaStorage.findMpaById(mpaId); }

    public List<Mpa> findAllMpa(){ return mpaStorage.findAllMpa();  }

}
