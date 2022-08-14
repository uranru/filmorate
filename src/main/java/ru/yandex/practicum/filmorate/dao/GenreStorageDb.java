package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorageInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Qualifier("genres")
public class GenreStorageDb implements GenreStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    public GenreStorageDb(JdbcTemplate jdbcTemplate){ this.jdbcTemplate=jdbcTemplate;}

    @Override
    public Genre findGenreById(Integer genreId) {
        try {
            Genre genre = jdbcTemplate.queryForObject("" +
                            "SELECT genre_id, genre_name " +
                            "FROM GENRES " +
                            "WHERE genre_id = ?",
                    this::mapRowToGenre, genreId);
            return genre;
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "Object not Found");
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("" +
                        "SELECT genre_id, genre_name  " +
                        "FROM GENRES ORDER BY genre_id ASC;",
                this::mapRowToGenre);
    }

    @Override
    public List<Map> findAllFilmsAndGenres() {
        List mapList = jdbcTemplate.queryForList("" +
                "SELECT FG.FILM_ID, G.GENRE_ID,G.GENRE_NAME " +
                "FROM GENRES AS G " +
                "RIGHT JOIN FILMS_GENRES FG ON G.GENRE_ID = FG.GENRE_ID ");
        return mapList;
    }

    @Override
    public List<Genre> findGenreByFilmId(Long filmId) {
        return jdbcTemplate.query("" +
                        "SELECT G.GENRE_ID,G.GENRE_NAME " +
                        "FROM GENRES AS G " +
                        "RIGHT JOIN FILMS_GENRES FG ON G.GENRE_ID = FG.GENRE_ID " +
                        "WHERE FG.FILM_ID = ?;",
                this::mapRowToGenre, filmId);
    }

    @Override
    public Integer updateFilmGenre(Long filmId, Integer genreId) {
        return jdbcTemplate.update("" +
                        "INSERT INTO FILMS_GENRES (film_id, GENRE_ID) " +
                        "VALUES (?,?)",
                filmId, genreId);
    }

    @Override
    public Integer deleteFilmAllGenres(Long filmId) {
        return jdbcTemplate.update("" +
                        "DELETE FROM FILMS_GENRES " +
                        "WHERE FILM_ID = ?",
                filmId);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
