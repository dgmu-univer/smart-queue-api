package ru.dgmu.smartqueue.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dto.UserDto;
import ru.dgmu.smartqueue.dto.UserDto.UserInfoDto;
import ru.dgmu.smartqueue.entity.User;

@RestController
@Slf4j
public class UserInfoController {
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUser(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            UserDto userDto = UserDto.fromEntity(user);
            return ResponseEntity.ok(userDto.getUserInfo());
        } catch (NullPointerException exception) {
            log.error("Authentication is null");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/you")
    public String getYou() {
        return "Привет, ";
    }

}