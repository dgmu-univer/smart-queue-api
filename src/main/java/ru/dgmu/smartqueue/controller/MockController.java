package ru.dgmu.smartqueue.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

  @GetMapping("/mock")
  public ResponseEntity<String> get() {
    return ResponseEntity.ok("Hello World");
  }

  @GetMapping("/set-data")
  public String setData(HttpSession session) {
    session.setAttribute("myKey", new String("Hello from Redis!"));
    return "Saved!";
  }

  @GetMapping("/get-data")
  public Object getData(HttpSession session) {
    return session.getAttribute("7d3b20f8-96f7-4d39-b525-45b31471cb92");
  }

}
