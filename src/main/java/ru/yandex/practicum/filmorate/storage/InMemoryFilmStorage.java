package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Qualifier("films")
public class InMemoryFilmStorage extends InMemoryUniversalStorage<Film>{
    protected final Long i = 0L;
    protected final Map<Long,Film> listObjects = new HashMap<>();
    @Override
    protected void setId(Film film, Long id) {
        film.setId(id);
    }

    @Override
    protected Long getId(Film film) {
        return film.getId();
    }

    @Override
    void checkObject(Film film) {
        updatePopularFilm();
    }

    Comparator<Film> comparator = new Comparator<Film>() {
        @Override
        public int compare(Film f1, Film f2) {
            if (f1.getId() == f2.getId()) {return 0;}
            if (f1.getListLikes().size() == f2.getListLikes().size()) {
                return -1;
            } else {
                return f2.getListLikes().size() - f1.getListLikes().size();
            }
        }
    };
    @Getter
    private Set<Film> listPopularFilms = new TreeSet<>(comparator);

    public void updatePopularFilm(){
        listPopularFilms.clear();
        for (Film popFilm: super.listObjects.values()) {
            listPopularFilms.add(popFilm);
        }
    }

}
