package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dao.FollowDao;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.FollowService;

import java.util.List;

@RestController
public class FollowController {
    private final FollowService followService;

    @Autowired
    public FollowController (FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/users/{userId}/follows/{followerId}")
    public User addSubscribtion(@PathVariable String userId,
                                @PathVariable String followerId) {
        return followService.addSubscription(userId, followerId);
    }

    @GetMapping("/users/{userId}/follows")
    public List<Post> getFollowFeed(@PathVariable String userId, @RequestParam (defaultValue = "10") int max) {
        return followService.getFollowFeed(userId, max);
    }
}
