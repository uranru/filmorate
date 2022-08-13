package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorageInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("mpa")
public class MpaStorageDb implements MpaStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    public MpaStorageDb(JdbcTemplate jdbcTemplate){ this.jdbcTemplate=jdbcTemplate;}

    @Override
    public Mpa findMpaById(Integer mpaId) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject("" +
                            "SELECT mpa_id, mpa_name " +
                            "FROM MPA " +
                            "WHERE mpa_id = ?",
                    this::mapRowToMpa, mpaId);
            return mpa;
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.resolve(404), "Object not Found");
        }
    }

    public List<Mpa> findAllMpa() {
        return jdbcTemplate.query("" +
                      "SELECT mpa_id, mpa_name  " +
                      "FROM MPA;",
                  this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

}
