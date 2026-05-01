package ru.dgmu.smartqueue.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.entity.User;
import ru.dgmu.smartqueue.service.UserService;

@RestController
@RequiredArgsConstructor
public class CreateAdminController {
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;


//  @GetMapping("/admin")
//  public void createAdmin() {
//    userService.create(new User(null, "admin", passwordEncoder.encode("password"), "admin@example.com", true, List.of()));
//  }
}
