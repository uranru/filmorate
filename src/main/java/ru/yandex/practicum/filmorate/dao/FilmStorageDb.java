package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorageInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
@Qualifier("films")
public class FilmStorageDb implements FilmStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    public FilmStorageDb(JdbcTemplate jdbcTemplate){ this.jdbcTemplate=jdbcTemplate;}
    private static final Logger log = LoggerFactory.getLogger(FilmStorageDb.class);

    @Override
    public List<Film> findAllFilms() {
        List<Film> allFilms = jdbcTemplate.query("" +
                        "SELECT film_id, film_name, film_description, film_release_date, film_duration, mpa_id " +
                        "FROM FILMS;",
                this::mapRowToFilm);
        for (Film film: allFilms) {
            film.setGenres(jdbcTemplate.query("" +
                            "SELECT G.GENRE_ID,G.GENRE_NAME " +
                            "FROM GENRES AS G " +
                            "RIGHT JOIN FILMS_GENRES FG ON G.GENRE_ID = FG.GENRE_ID " +
                            "WHERE FG.FILM_ID = ?;",
                    this::mapRowToGenre, film.getId()));
        }
        return allFilms;
    }

    @Override
    public Film addFilm(Film newFilm) {
        try {
            int result = jdbcTemplate.update("" +
                            "INSERT INTO FILMS (film_name, film_description, film_release_date, film_duration, mpa_id) " +
                            "VALUES (?,?,?,?,?)",
                    newFilm.getName(),
                    newFilm.getDescription(),
                    newFilm.getReleaseDate(),
                    newFilm.getDuration(),
                    newFilm.getMpa().getId());

            if (newFilm.getGenres() != null){
                for (Genre genre : newFilm.getGenres()) {
                    jdbcTemplate.update("" +
                                    "INSERT INTO FILMS_GENRES (film_id, GENRE_ID) " +
                                    "VALUES (?,?)",
                            findFilmByName(newFilm.getName()).getId(),
                            genre.getId());
                }
            }

            if (result > 0) {
                return findFilmByName(newFilm.getName());
            } else {
                throw new ResponseStatusException(
                        HttpStatus.resolve(400), "Object not Created");
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(
                   HttpStatus.resolve(400), "Object not Created");
        }


    }

    @Override
    public Film findFilmByName(String filmName) {
        try {
            Film film = jdbcTemplate.queryForObject("" +
                            "SELECT film_id, film_name, film_description, film_release_date, film_duration, mpa_id " +
                            "FROM FILMS " +
                            "WHERE film_name = ?",
                    this::mapRowToFilm, filmName);
            film.setGenres(jdbcTemplate.query("" +
                            "SELECT G.GENRE_ID,G.GENRE_NAME " +
                            "FROM GENRES AS G " +
                            "RIGHT JOIN FILMS_GENRES FG ON G.GENRE_ID = FG.GENRE_ID " +
                            "WHERE FG.FILM_ID = ?;",
                    this::mapRowToGenre, film.getId()));
            return film;
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "Object not Found");
        }
    }

    @Override
    public Film findFilmById(Long filmId) {
        try {
            Film film = jdbcTemplate.queryForObject("" +
                            "SELECT film_id, film_name, film_description, film_release_date, film_duration, mpa_id " +
                            "FROM FILMS " +
                            "WHERE film_id = ?",
                    this::mapRowToFilm, filmId);
            film.setGenres(jdbcTemplate.query("" +
                            "SELECT G.GENRE_ID,G.GENRE_NAME " +
                            "FROM GENRES AS G " +
                            "RIGHT JOIN FILMS_GENRES FG ON G.GENRE_ID = FG.GENRE_ID " +
                            "WHERE FG.FILM_ID = ?;",
                    this::mapRowToGenre, filmId));
            return film;
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "Object not Found");
        }
    }

    @Override
    public List<Film> findPopularFilms(Integer countFilms) {
        int rowLimit;
        if (countFilms == null) {
            rowLimit = 10;
        } else {
            rowLimit = countFilms;
        }

        return jdbcTemplate.query("" +
                        "SELECT f.film_id, f.film_name, f.film_description, f.film_release_date, f.film_duration, f.mpa_id, " +
                        "(SELECT count(*) FROM FILMS_FAVORITE AS ff WHERE FF.FILM_ID=F.FILM_ID ) AS count " +
                        "FROM FILMS as f " +
                        "ORDER BY count " +
                        "DESC LIMIT ?;",
                this::mapRowToFilm,
                rowLimit);
    }

    @Override
    public void deleteFilmById(Long filmId) {
        try {
            int result = jdbcTemplate.update("" +
                        "DELETE FROM FILMS " +
                        "WHERE film_id = ?",
                    filmId);
            if (result == 0) {
                throw new ResponseStatusException(
                        HttpStatus.resolve(404), "Object not Found");
            }
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(400),"");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            int result = jdbcTemplate.update("" +
                            "UPDATE FILMS " +
                            "SET film_name = ?, film_description= ?, film_release_date = ?, film_duration = ?, mpa_id = ? " +
                            "WHERE FILM_ID = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            jdbcTemplate.update("" +
                            "DELETE FROM FILMS_GENRES " +
                            "WHERE FILM_ID = ?",
                    film.getId());

            if (film.getGenres() != null){
                Set<Integer> setGenres = new TreeSet<>();
                for (Genre genre : film.getGenres()) {
                    setGenres.add(genre.getId());
                }

                for (Integer gereId : setGenres) {
                    jdbcTemplate.update("" +
                                    "INSERT INTO FILMS_GENRES (film_id, GENRE_ID) " +
                                    "VALUES (?,?) ",
                            film.getId(),
                            gereId);
                }

                film.setGenres(jdbcTemplate.query("" +
                                "SELECT G.GENRE_ID,G.GENRE_NAME " +
                                "FROM GENRES AS G " +
                                "RIGHT JOIN FILMS_GENRES FG ON G.GENRE_ID = FG.GENRE_ID " +
                                "WHERE FG.FILM_ID = ?;",
                        this::mapRowToGenre, film.getId()));
            }

            if (result > 0) {
                return findFilmByName(film.getName());
            } else {
                throw new ResponseStatusException(
                        HttpStatus.resolve(404), "");
            }
        } catch (Exception exception) {
            throw new ResponseStatusException(
                   HttpStatus.resolve(404), "");
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update("" +
                        "INSERT INTO films_favorite (film_id, user_id) " +
                        "VALUES (?,?)",
                filmId,
                userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        int result = jdbcTemplate.update("DELETE FROM films_favorite " +
                        "WHERE FILM_ID = ? " +
                        "AND USER_ID = ?",
                filmId,
                userId);
        if (result == 0) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "");
        }
    }

    private Mpa findMpaById(Integer mpaId) {
        try {
            return jdbcTemplate.queryForObject("SELECT MPA_ID, MPA_NAME " +
                    "FROM MPA " +
                    "WHERE MPA_ID = ?", this::mapRowToMpa, mpaId);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "Object not Found");
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("film_description"))
                .releaseDate(LocalDate.parse(resultSet.getString("film_release_date")))
                .duration(resultSet.getLong("film_duration"))
                .mpa(findMpaById(resultSet.getInt("mpa_id")))
                .build();
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
