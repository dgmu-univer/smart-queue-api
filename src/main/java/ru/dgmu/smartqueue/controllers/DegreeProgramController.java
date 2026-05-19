package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramsWithPeriodDto;
import ru.dgmu.smartqueue.exception.ApiError;
import ru.dgmu.smartqueue.services.DegreeProgramService;

@RestController
@Tag(name = "Degree programs", description = "Управление программами образовния")
public class DegreeProgramController {

  private final DegreeProgramService degreeProgramService;

  public DegreeProgramController(DegreeProgramService degreeProgramService) {
    this.degreeProgramService = degreeProgramService;
  }

  @GetMapping("/degree-programs")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<List<DegreeProgramDto>> getAllDegreePrograms() {
    return ResponseEntity.ok(degreeProgramService.getDegreePrograms());
  }

  @Operation(description = "Создание программы образования")
  @PostMapping("/degree-programs")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> createDegreeProgram(@RequestBody @Valid DegreeProgramDto degreeProgramDto) {
    degreeProgramService.createDegreeProgram(degreeProgramDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(description = "Удалить программу образования")
  @DeleteMapping("/degree-programs/{id}")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public void deleteDegreeProgramByUserId(@PathVariable @Parameter(description = "ID", required = true) Long id) {
    degreeProgramService.deleteDegreeProgram(id);
  }

  @Operation(description = "Получение всех программ с интервалом дат приема")
  @GetMapping("/public/degree-programs")
  public ResponseEntity<DegreeProgramsWithPeriodDto> getDegreePrograms() {
    return ResponseEntity.ok(degreeProgramService.getDegreeProgramsPresentations());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handle(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError("Запись с таким "));
  }
}