package ru.yandex.practicum.catsgram.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.dao.PostDao;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component
public class PostDaoImpl implements PostDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post addPost(Post post) {
        SqlRowSet rawUser = jdbcTemplate.queryForRowSet("SELECT id, username, nickname FROM cat_user WHERE " +
                "id =?", post.getAuthor().getId());
        User user = new User();
        if (rawUser.next()) {
            user.setId(rawUser.getString("id"));
            user.setUsername(rawUser.getString("username"));
            user.setNickname(rawUser.getString("nickname"));
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id%s не найден.", post.getAuthor().getId()));
        }
        String sql = "INSERT INTO cat_post(author_id, description, photo_url, creation_date) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, post.getAuthor().getId(), post.getDescription(), post.getPhotoUrl(),
                post.getCreationDate());
        SqlRowSet rawPost = jdbcTemplate
                .queryForRowSet("SELECT id, author_id, description, photo_url, creation_date FROM cat_post " +
                                "WHERE author_id = ? AND description = ? AND photo_url = ?", post.getAuthor().getId(),
                        post.getDescription(), post.getPhotoUrl());
        if (rawPost.next()) {
            return new Post(rawPost.getInt("id"), user, rawPost.getString("description"),
                    rawPost.getString("photo_url"),
                    rawPost.getDate("creation_date").toLocalDate());
        }
        return post;
    }

    @Override
    public Collection<Post> findPostsByUser(User user) {
        String sql = "select * from cat_post where author_id = ? order by creation_date desc";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makePost(user, rs), user.getId());
    }

    private Post makePost(User user, ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String description = rs.getString("description");
        String photoUrl = rs.getString("photo_url");
        LocalDate creationDate = rs.getDate("creation_date").toLocalDate();

        return new Post(id, user, description, photoUrl, creationDate);
    }
}
