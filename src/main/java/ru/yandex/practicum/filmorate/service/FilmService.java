package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dao.FilmStorageDb;
import ru.yandex.practicum.filmorate.dao.GenreStorageDb;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Service
@Qualifier("films")
public class FilmService {

    private final FilmStorageDb filmStorage;
    private final GenreStorageDb genreStorage;

    @Autowired
    public FilmService(FilmStorageDb storage, GenreStorageDb genreStorage) {
        this.filmStorage = storage;
        this.genreStorage = genreStorage;
    }

    public Film addFilm(Film film) {
        try {
            if (filmStorage.addFilm(film) > 0) {
                Film filmReturn = filmStorage.findFilmByName(film.getName());

                if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                    for (Genre genre: film.getGenres()) {
                        genreStorage.updateFilmGenre(filmReturn.getId(),genre.getId());
                    }
                    filmReturn.setGenres(film.getGenres());
                }
                return filmReturn;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.resolve(400), "Object not Created");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(400), "Object not Created");
        }
    }

    public Film findFilmById(Long filmId){
        try {
            Film findFilm = filmStorage.findFilmById(filmId);
            findFilm.setGenres(genreStorage.findGenreByFilmId(findFilm.getId()));
            return findFilm;
        }  catch (
            EmptyResultDataAccessException exception) {
                throw new ResponseStatusException(
                HttpStatus.resolve(404), "Object not Found");
        }
    }

    public List<Film> findAllFilms() {
        List<Film> allFilms = filmStorage.findAllFilms();

        List<Map> allFilmsAndGenresMap = genreStorage.findAllFilmsAndGenres();
        if (allFilmsAndGenresMap.size() > 0) {
            Map<Long,List<Genre>> filmsMap = new HashMap<>();
            for (Map map: allFilmsAndGenresMap) {
                Long filmId= Long.parseLong(map.get("FILM_ID").toString());
                Genre genre = new Genre(Integer.parseInt(map.get("GENRE_ID").toString()),map.get("GENRE_NAME").toString());
                List<Genre> genres = new ArrayList<>();
                if (filmsMap.containsKey(filmId)) {
                    genres = filmsMap.get(filmId);
                    genres.add(genre);
                    filmsMap.put(filmId,genres);
                } else {
                    genres.add(genre);
                    filmsMap.put(filmId,genres);
                }
            }

            for (Film film: allFilms) {
                if (filmsMap.containsKey(film.getId())) {
                    film.setGenres(filmsMap.get(film.getId()));
                }
            }
        }


        return allFilms;
    }

    public List<Film> findPopularFilms(Integer filmsCount){ return filmStorage.findPopularFilms(filmsCount); }

    public void deleteFilmById(Long filmId){ filmStorage.deleteFilmById(filmId); }

    public Film updateFilm(Film film) {
        if (filmStorage.updateFilm(film) > 0) {
            Film filmReturn = filmStorage.findFilmById(film.getId());

            // Начало: Актуализация жанров
            genreStorage.deleteFilmAllGenres(film.getId());
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                Set<Integer> genresSet = new TreeSet<>();
                for (Genre genre : film.getGenres()) {
                    genresSet.add(genre.getId());
                }
                for (Integer genreID : genresSet) {
                    genreStorage.updateFilmGenre(film.getId(), genreID);
                }
            }
            filmReturn.setGenres(genreStorage.findGenreByFilmId(film.getId()));
            // Конец: Актуализация жанров

            return filmReturn;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "");
        }
    }

    public void addLike(Long filmId, Long userId) { filmStorage.addLike(filmId,userId); }

    public void deleteLike(Long filmId, Long userId) { filmStorage.deleteLike(filmId,userId); }
}
