package ru.yandex.practicum.catsgram.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.dao.UserDao;
import ru.yandex.practicum.catsgram.model.User;

import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update("INSERT INTO cat_user(id, username, nickname) VALUES (?, ?, ?)", user.getId(),
                user.getUsername(), user.getNickname());
        SqlRowSet rawUser = jdbcTemplate
                .queryForRowSet("SELECT id, username, nickname FROM cat_user WHERE id = ?", user.getId());
        if (rawUser.next()) {
            return new User(rawUser.getString("id"), rawUser.getString("username"),
                    rawUser.getString("nickname"));
        }
        return user;
    }

    @Override
    public Optional<User> findUserById(String id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from cat_user where id = ?", id);
        if (userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("id"),
                    userRows.getString("nickname"));
            User user = new User(
                    userRows.getString("id"),
                    userRows.getString("username"),
                    userRows.getString("nickname"));
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}

