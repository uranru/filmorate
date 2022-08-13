package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorageInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
                        "FROM GENRES;",
                this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
