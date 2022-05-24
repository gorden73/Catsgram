package ru.yandex.practicum.catsgram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.controller.SimpleController;
import ru.yandex.practicum.catsgram.exceptions.IncorrectParameterException;
import ru.yandex.practicum.catsgram.exceptions.PostNotFoundException;
import ru.yandex.practicum.catsgram.exceptions.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(SimpleController.class);
    private final List<Post> posts = new ArrayList<>();
    private final UserService userService;
    private static Integer id = 0;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    private static Integer getNextId() {
        return id++;
    }

    public List<Post> findAll(Integer from, String sort, Integer size) {
        log.info("Количество постов: {}", posts.size());
        List<Post> postList = posts;
        postList.sort(Comparator.comparing(Post::getCreationDate));
        if (sort.equals("desc")) {
            postList.sort(Comparator.comparing(Post::getCreationDate).reversed());
        }
        if (size < postList.size() && from < postList.size() && (size + from) < postList.size()) {
            return postList.subList(from, size + from);
        }
        if (from > postList.size()) {
            throw new IllegalArgumentException(String.format("Значение from=%d больше количества постов.", from));
        }
        return postList.subList(from, postList.size());
    }

    public Post create(Post post) throws UserNotFoundException {
        if (userService.getUserByEmail(post.getAuthor()) == null) {
            throw new UserNotFoundException("Пользователь " + post.getAuthor() + " не найден.");
        }
        log.debug("Сохраненный объект: {}", post);
        post.setId(getNextId());
        posts.add(post);
        return post;
    }

    public Post getPostById(Integer id) {
        if (id == null || id > posts.size()-1 || id < 0) {
           throw new PostNotFoundException("Пост не найден.");
        }
        return posts.get(id);
    }

    public Post findPostById(Integer id) {
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(String.format("Пост № %d не найден", id)));
    }

    public List<Post> findPostByUserEmail(String email, Integer size, String sort) {
        return posts.stream().filter(p -> email.equals(p.getAuthor())).sorted((p0, p1) -> {
            int comp = p0.getCreationDate().compareTo(p1.getCreationDate()); //прямой порядок сортировки
            if(sort.equals("desc")){
                comp = -1 * comp; //обратный порядок сортировки
            }
            return comp;
        }).limit(size).collect(Collectors.toList());
    }
}
