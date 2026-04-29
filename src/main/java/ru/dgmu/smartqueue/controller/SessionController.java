package ru.dgmu.smartqueue.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    @GetMapping("/me")
    public String getCurrentUser(@AuthenticationPrincipal User user) {
        return "Привет, " + user.getUsername();
    }

}