package ru.dgmu.smartqueue.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramsWithPeriodDto;
import ru.dgmu.smartqueue.exception.ApiError;
import ru.dgmu.smartqueue.services.DegreeProgramService;

@RestController
@RequestMapping("/degree-programs")
public class DegreeProgramController {

  private final DegreeProgramService degreeProgramService;

  public DegreeProgramController(DegreeProgramService degreeProgramService) {
    this.degreeProgramService = degreeProgramService;
  }

  @GetMapping
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<List<DegreeProgramDto>> getAllDegreePrograms() {
    return ResponseEntity.ok(degreeProgramService.getDegreePrograms());
  }

  @PostMapping
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> createDegreeProgram(@RequestBody @Valid DegreeProgramDto degreeProgramDto) {
    degreeProgramService.createDegreeProgram(degreeProgramDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public void deleteDegreeProgramByUserId(@PathVariable Long id) {
    degreeProgramService.deleteDegreeProgram(id);
  }

  @GetMapping("/public")
  public ResponseEntity<DegreeProgramsWithPeriodDto> getDegreePrograms() {
    return ResponseEntity.ok(degreeProgramService.getDegreeProgramsPresentations());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handle(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError("Запись с таким "));
  }
}