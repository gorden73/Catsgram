package ru.yandex.practicum.catsgram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.controller.SimpleController;
import ru.yandex.practicum.catsgram.exceptions.InvalidEmailException;
import ru.yandex.practicum.catsgram.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.catsgram.exceptions.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(SimpleController.class);
    private final Map<String, User> users = new HashMap<>();

    public Collection<User> findAll() {
        log.debug("Сейчас пользователей: {}", users.size());
        return users.values();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public User create(User user) {
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")
                || user.getEmail().contains(" ")) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        if(users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        users.put(user.getEmail(), user);
        log.debug("Сохраненный объект: {}", user);
        return user;
    }

    public User update(User user) {
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        users.put(user.getEmail(), user);
        return user;
    }

    public User getUserByEmail(String email) {
        if (!users.containsKey(email)) {
            throw new UserNotFoundException(String.format("Пользователь %s не найден.", email));
        }
        return users.get(email);
    }
}
