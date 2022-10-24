package ru.yandex.practicum.catsgram.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.dao.FollowDao;
import ru.yandex.practicum.catsgram.dao.PostDao;
import ru.yandex.practicum.catsgram.dao.UserDao;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class FollowDaoImpl implements FollowDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final PostDao postDao;

    @Autowired
    public FollowDaoImpl(JdbcTemplate jdbcTemplate, UserDao userDao, PostDao postDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.postDao = postDao;
    }

    @Override
    public User addSubscription(String userId, String followerId) {
        SqlRowSet rawUser = jdbcTemplate.queryForRowSet("SELECT id, username, nickname FROM cat_user WHERE " +
                "id =?", userId);
        if (rawUser.next()) {
            SqlRowSet rawFollower = jdbcTemplate.queryForRowSet("SELECT id, username, nickname FROM cat_user WHERE " +
                    "id =?", followerId);
            User follower = new User();
            if (rawFollower.next()) {
                follower.setId(rawFollower.getString("id"));
                follower.setUsername(rawFollower.getString("username"));
                follower.setNickname(rawFollower.getString("nickname"));
            } else {
                throw new UserNotFoundException(String.format("Пользователь с id%s не найден.", followerId));
            }
            jdbcTemplate.update("INSERT INTO cat_follow(user_id, follow_id) VALUES(?, ?)", userId, followerId);
            return follower;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id%s не найден.", userId));
        }
    }

    @Override
    public List<Post> getFollowFeed(String userId, int max) {
        String sql = "SELECT cf.user_id, " +
                "cu.username, " +
                "cu.nickname, " +
                "cp.id AS post_id, " +
                "cp.description, " +
                "cp.photo_url, " +
                "cp.creation_date " +
                "FROM cat_post AS cp " +
                "INNER JOIN cat_user AS cu ON cu.id = cp.author_id " +
                "INNER JOIN cat_follow AS cf ON cf.user_id = cu.id " +
                "WHERE author_id IN (SELECT id " +
                "FROM cat_user " +
                "WHERE id IN (SELECT follow_id " +
                "FROM cat_follow " +
                "WHERE user_id = ?)) " +
                "GROUP BY cf.user_id, " +
                "cu.username, " +
                "cu.nickname, " +
                "post_id, " +
                "cp.description, " +
                "cp.photo_url, " +
                "cp.creation_date " +
                "ORDER BY cp.creation_date DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFollow(rs), userId, max);
    }

    private Post makeFollow(ResultSet rs) throws SQLException {
        String userId = rs.getString("user_id");
        String userName = rs.getString("username");
        String nickName = rs.getString("nickname");
        String description = rs.getString("description");
        String photo_url = rs.getString("photo_url");
        Integer postId = rs.getInt("post_id");
        LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
        return new Post(postId, new User(userId, userName, nickName), description, photo_url, creationDate);
    }
}
