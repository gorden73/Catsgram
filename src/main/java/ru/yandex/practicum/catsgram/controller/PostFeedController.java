package ru.yandex.practicum.catsgram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.exceptions.IncorrectParameterException;
import ru.yandex.practicum.catsgram.model.FriendsPosts;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("/feed/friends")
public class PostFeedController {

    private final PostService postService;

    public PostFeedController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    List<Post> getFriendsFeed(@RequestBody String params) {
        ObjectMapper objectMapper = new ObjectMapper();
        FriendsPosts friendsPosts;
        try {
            String paramsFromString = objectMapper.readValue(params, String.class);
            friendsPosts = objectMapper.readValue(paramsFromString, FriendsPosts.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Невалидный формат json", e);
        }

        if (!(friendsPosts.getSort().equals("asc") || friendsPosts.getSort().equals("desc"))) {
            throw new IncorrectParameterException("sort");
        }
        if (friendsPosts.getSize() == null || friendsPosts.getSize() <= 0) {
            throw new IncorrectParameterException("size");
        }
        if (friendsPosts.getFriends().isEmpty()) {
            throw new IncorrectParameterException("friendsEmails");
        }

        if (friendsPosts != null) {
            List<Post> result = new ArrayList<>();
            for (String friend : friendsPosts.getFriends()) {
                result.addAll(postService.findPostsByUser(friend, friendsPosts.getSize(), friendsPosts.getSort()));
            }
            return result;
        } else {
            throw new RuntimeException("Неверно заполнены параметры");
        }
    }


}
