package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dao.FollowDao;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.List;

@Service
public class FollowService {
    private final FollowDao followDao;

    @Autowired
    public FollowService(FollowDao followDao) {
        this.followDao = followDao;
    }

    public List<Post> getFollowFeed(String userId, int max) {
        return followDao.getFollowFeed(userId, max);
    }
}
