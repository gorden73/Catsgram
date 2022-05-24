package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exceptions.IncorrectParameterException;
import ru.yandex.practicum.catsgram.exceptions.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> findAll(@RequestParam(defaultValue = "asc") String sort,
                              @RequestParam(defaultValue = "0") Integer from,
                              @RequestParam(defaultValue = "10") Integer size) {
        if (!(sort.equals("asc") || sort.equals("desc"))) {
            throw new IncorrectParameterException("sort");
        }
        if (from < 0){
            throw new IncorrectParameterException("from");
        }
        if (size <= 0){
            throw new IncorrectParameterException("size");
        }
        return postService.findAll(from, sort, size);
    }

    @PostMapping
    public void create(@RequestBody Post post) throws UserNotFoundException {
        postService.create(post);
    }

    @GetMapping("/post/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }
}