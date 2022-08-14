package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorageInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


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
        return allFilms;
    }

    @Override
    public Integer addFilm(Film newFilm) {
         return jdbcTemplate.update("" +
                            "INSERT INTO FILMS (film_name, film_description, film_release_date, film_duration, mpa_id) " +
                            "VALUES (?,?,?,?,?)",
                    newFilm.getName(),
                    newFilm.getDescription(),
                    newFilm.getReleaseDate(),
                    newFilm.getDuration(),
                    newFilm.getMpa().getId());
    }

    @Override
    public Film findFilmByName(String filmName) {
        return jdbcTemplate.queryForObject("" +
                            "SELECT film_id, film_name, film_description, film_release_date, film_duration, mpa_id " +
                            "FROM FILMS " +
                            "WHERE film_name = ?",
                    this::mapRowToFilm, filmName);
        }

    @Override
    public Film findFilmById(Long filmId) {
        return jdbcTemplate.queryForObject("" +
                            "SELECT film_id, film_name, film_description, film_release_date, film_duration, mpa_id " +
                            "FROM FILMS " +
                            "WHERE film_id = ?",
                    this::mapRowToFilm, filmId);
        }

    @Override
    public List<Film> findPopularFilms(Integer countFilms) {
        return jdbcTemplate.query("" +
                        "SELECT f.film_id, f.film_name, f.film_description, f.film_release_date, f.film_duration, f.mpa_id, " +
                        "(SELECT count(*) FROM FILMS_FAVORITE AS ff WHERE FF.FILM_ID=F.FILM_ID ) AS count " +
                        "FROM FILMS as f " +
                        "ORDER BY count " +
                        "DESC LIMIT ?;",
                this::mapRowToFilm,
                countFilms);
    }

    @Override
    public Integer deleteFilmById(Long filmId) {
        return jdbcTemplate.update("" +
                        "DELETE FROM FILMS " +
                        "WHERE film_id = ?",
                    filmId);
        }

    @Override
    public Integer updateFilm(Film film) {
         return jdbcTemplate.update("" +
                            "UPDATE FILMS " +
                            "SET film_name = ?, film_description= ?, film_release_date = ?, film_duration = ?, mpa_id = ? " +
                            "WHERE FILM_ID = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
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
        return jdbcTemplate.queryForObject("SELECT MPA_ID, MPA_NAME " +
                    "FROM MPA " +
                    "WHERE MPA_ID = ?", this::mapRowToMpa, mpaId);
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

}
