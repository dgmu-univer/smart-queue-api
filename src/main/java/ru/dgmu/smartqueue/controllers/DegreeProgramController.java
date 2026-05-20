package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.DegreeProgramsWithPeriodDto;
import ru.dgmu.smartqueue.services.DegreeProgramService;

@RestController
@Tag(name = "Degree programs", description = "Управление программами образования")
@RequiredArgsConstructor
@Slf4j
public class DegreeProgramController {

  private final DegreeProgramService degreeProgramService;

  @GetMapping("/degree-programs")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @Operation(description = "Получение всех программ образования")
  public ResponseEntity<List<DegreeProgramDto>> getAllDegreePrograms() {
    log.debug("Getting all degree programs");
    List<DegreeProgramDto> programs = degreeProgramService.getDegreePrograms();
    log.debug("Found {} degree programs", programs.size());
    return ResponseEntity.ok(programs);
  }

  @Operation(description = "Создание программы образования")
  @PostMapping("/degree-programs")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> createDegreeProgram(@RequestBody @Valid DegreeProgramDto degreeProgramDto) {
    log.info("Creating degree program: {}", degreeProgramDto.name());
    degreeProgramService.createDegreeProgram(degreeProgramDto);
    log.info("Degree program created successfully: {}", degreeProgramDto.name());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(description = "Удалить программу образования")
  @DeleteMapping("/degree-programs/{id}")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> deleteDegreeProgramById(
      @PathVariable @Parameter(description = "ID программы образования", required = true) Long id) {
    log.info("Deleting degree program with ID: {}", id);
    degreeProgramService.deleteDegreeProgram(id);
    log.info("Degree program deleted successfully with ID: {}", id);
    return ResponseEntity.ok().build();
  }

  @Operation(description = "Получение всех программ с интервалом дат приема")
  @GetMapping("/public/degree-programs")
  public ResponseEntity<DegreeProgramsWithPeriodDto> getDegreePrograms() {
    log.debug("Getting degree programs with period information");
    DegreeProgramsWithPeriodDto programs = degreeProgramService.getDegreeProgramsPresentations();
    return ResponseEntity.ok(programs);
  }
}