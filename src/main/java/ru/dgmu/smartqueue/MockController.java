package ru.dgmu.smartqueue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

  @GetMapping("/mock")
  public ResponseEntity<String> get() {
    return ResponseEntity.ok("Hello World");
  }
}
