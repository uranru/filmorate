package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorageInterface;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("users")
public class UserStorageDb implements UserStorageInterface {
    private final JdbcTemplate jdbcTemplate;
    public UserStorageDb(JdbcTemplate jdbcTemplate){ this.jdbcTemplate=jdbcTemplate;}
    private static final Logger log = LoggerFactory.getLogger(UserStorageDb.class);


    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query("" +
                    "SELECT user_id, user_login, user_name, user_email, user_birthday " +
                    "FROM USERS;",
                this::mapRowToUser);
    }


    @Override
    public User findUserById(Long id) {
        return jdbcTemplate.queryForObject("SELECT user_id, user_login, user_name, user_email, user_birthday " +
                    "FROM USERS " +
                    "WHERE user_id = ?", this::mapRowToUser, id);
        }

    @Override
    public User findUserByLogin(String login) {
        return jdbcTemplate.queryForObject("" +
                        "SELECT user_id, user_login, user_name, user_email, user_birthday " +
                        "FROM USERS " +
                        "WHERE USER_LOGIN = ?",
                    this::mapRowToUser, login);
        }

    @Override
    public Integer addUser(User newUser) {
        return jdbcTemplate.update("" +
                            "INSERT INTO USERS (user_name, user_email, user_login, user_birthday) " +
                            "VALUES (?,?,?,?)",
                    newUser.getName(),
                    newUser.getEmail(),
                    newUser.getLogin(),
                    newUser.getBirthday());
        }

    @Override
    public Integer updateUser(User user) {
        return jdbcTemplate.update("UPDATE USERS " +
                            "SET user_name=?, user_email=?, user_login=?, user_birthday=? " +
                            "WHERE user_id=?",
                    user.getName(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getId());
        }

    @Override
    public Integer deleteUserById(Long id) {
        return jdbcTemplate.update("DELETE FROM users " +
                    "WHERE user_id = ?", id);
        }

    @Override
    public Integer addFriend(Long userId, Long friendId) {
        return jdbcTemplate.update("INSERT INTO USERS_FRIENDS (user_id, FRIEND_ID) " +
                            "VALUES (?,?)",
                             userId,
                             friendId);
        }

    @Override
    public List<User> findFriends(Long userId) {
        return jdbcTemplate.query("" +
                    "SELECT u.user_id as user_id, u.user_login, u.user_name, u.user_email, u.user_birthday " +
                    "FROM USERS_FRIENDS as f " +
                    "LEFT JOIN USERS as u ON u.user_id = f.FRIEND_ID " +
                    "WHERE ? = f.user_id;",
            this::mapRowToUser, userId);
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long friendId) {
        return jdbcTemplate.query("" +
                    "SELECT DISTINCT u.user_id as user_id, u.user_login, u.user_name, u.user_email, u.user_birthday " +
                    "FROM USERS_FRIENDS as f " +
                    "LEFT JOIN USERS as u ON u.user_id IN (f.FRIEND_ID,f.USER_ID) " +
                    "WHERE (? IN (f.user_id,f.FRIEND_ID) OR ? IN (f.user_id,f.FRIEND_ID))" +
                    "AND u.user_id NOT IN (?,?);",
            this::mapRowToUser, userId, friendId, userId, friendId);
    }

    @Override
    public Integer deleteFriend(Long userId, Long friendId) {
        return jdbcTemplate.update("DELETE FROM users_friends " +
                    "WHERE user_id IN (?,?) " +
                    "AND friend_id IN (?,?);",
                userId, friendId, userId, friendId);
        }

    @Override
    public List viewListObjects() {
        return null;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .login(resultSet.getString("user_login"))
                .name(resultSet.getString("user_name"))
                .email(resultSet.getString("user_email"))
                .birthday(LocalDate.parse(resultSet.getString("user_birthday")))
                .build();
    }
}
