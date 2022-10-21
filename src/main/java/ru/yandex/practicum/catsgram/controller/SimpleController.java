package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.service.HackCatService;

import javax.annotation.PostConstruct;

@RestController
public class SimpleController {

    private final HackCatService hackCatService;

    @Autowired
    public SimpleController(HackCatService hackCatService) {
        this.hackCatService = hackCatService;
    }

    @GetMapping("/do-hack")
    public void doHack() {
        System.out.println(hackCatService.doHackNow());
    }

    @GetMapping("/home")
    public String homePage() {
        return "Котограм";
    }
}
