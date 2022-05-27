package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.dao.FollowDao;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.FollowService;

import java.util.List;

@RestController
public class FollowController {
    private final FollowService followService;

    @Autowired
    public FollowController (FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/user/follows")
    public List<Post> getFollowFeed(@RequestParam String userId, @RequestParam (defaultValue = "10") int max) {
        return followService.getFollowFeed(userId, max);
    }
}
