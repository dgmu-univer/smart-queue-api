package ru.dgmu.smartqueue.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.MockJsonDto;

@RestController
@Slf4j
@RequestMapping("/public")
public class PublicController {

  @GetMapping("/mock")
  public ResponseEntity<MockJsonDto> getMockJson() {
    return ResponseEntity.ok(new MockJsonDto("name", "description"));
  }


}
