package ru.yandex.practicum.catsgram.dao;

import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.util.List;

public interface FollowDao {

    User addSubscription(String userId, String followerId);

    List<Post> getFollowFeed(String userId, int max);
}
