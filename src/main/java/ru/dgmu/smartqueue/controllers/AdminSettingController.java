package ru.dgmu.smartqueue.controllers;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.DegreeProgramDto;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.exception.ApiError;
import ru.dgmu.smartqueue.services.AdminSettingService;

@RestController
@RequestMapping("/admin-settings")
@PreAuthorize(value = "hasAuthority('ADMIN')")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Admin Settings", description = "Управление настройками администратора")
@SecurityRequirement(name = "bearerAuth")
public class AdminSettingController {

  private final AdminSettingService adminSettingService;

  // todo потом переделать под response если нужно

  @GetMapping("/periods")
  @Operation(summary = "Получить настройки периода", description = "Возвращает текущие настройки периода работы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки периода успешно получены", content = @Content(schema = @Schema(implementation = PeriodSettingsDto.class))),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<PeriodSettingsDto> getPeriodSettings() {
    return ResponseEntity.ok(adminSettingService.getPeriodSettings());
  }

  @PatchMapping("/periods")
  @Operation(summary = "Обновить настройки периода", description = "Обновляет настройки периода работы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки периода успешно обновлены"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<Void> patchPeriodSettings(
      @RequestBody @Parameter(description = "Обновленные настройки периода", required = true) PeriodSettingsDto updatedPeriodSettingsDto) {
    adminSettingService.updatePeriodSettings(updatedPeriodSettingsDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/slots")
  @Operation(summary = "Получить настройки слотов", description = "Возвращает текущие настройки слотов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки слотов успешно получены", content = @Content(schema = @Schema(implementation = SlotSettingsDto.class))),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<SlotSettingsDto> getSlots() {
    return ResponseEntity.ok(adminSettingService.getSlotSettings());
  }

  @PatchMapping("/slots")
  @Operation(summary = "Обновить настройки слотов", description = "Обновляет настройки слотов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки слотов успешно обновлены"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<Void> patchSlotsSettings(
      @RequestBody @Parameter(description = "Обновленные настройки слотов", required = true) SlotSettingsDto updatedSlotSettingsDto) {
    adminSettingService.updateSlotSettings(updatedSlotSettingsDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/non-working-days")
  @Operation(summary = "Получить нерабочие дни", description = "Возвращает список нерабочих дней")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список нерабочих дней успешно получен"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<List<LocalDate>> getNonWorkingDays() {
    return ResponseEntity.ok(adminSettingService.getNonWorkingDays());
  }

  @PutMapping("/non-working-days")
  @Operation(summary = "Обновить нерабочие дни", description = "Обновляет список нерабочих дней")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список нерабочих дней успешно обновлен"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<List<LocalDate>> putNonWorkingDays(
      @RequestBody @Parameter(description = "Обновленный список нерабочих дней", required = true) List<LocalDate> updatedNonWorkingDays) {
    return ResponseEntity.ok(adminSettingService.updateNonWorkingDays(updatedNonWorkingDays));
  }

  @GetMapping("/excluede-slots")
  @Operation(summary = "Получить исключенные слоты", description = "Возвращает список исключенных слотов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список исключенных слотов успешно получен"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<List<ExcludedSlotSettingsDto>> getExcluedeSlots() {
    return ResponseEntity.ok(adminSettingService.getExcludedSlots());
  }

  @PostMapping("/excluede-slots")
  @Operation(summary = "Создать исключенный слот", description = "Создает новый исключенный слот")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Исключенный слот успешно создан"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<Void> createExcludeSlot(
      @RequestBody @Parameter(description = "Данные исключенного слота", required = true) ExcludedSlotSettingsDto excludedSlotSettingsDto) {
    adminSettingService.createExcludedSlot(excludedSlotSettingsDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/excluede-slots/{id}")
  @Operation(summary = "Удалить исключенный слот", description = "Удаляет исключенный слот по ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Исключенный слот успешно удален"),
      @ApiResponse(responseCode = "404", description = "Исключенный слот не найден"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  public ResponseEntity<Void> getExcluedeSlots(@PathVariable @Parameter(description = "ID исключенного слота", required = true) Long id) {
    adminSettingService.deleteExcludedSlot(id);
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ApiError> handle(DataIntegrityViolationException e) {
    log.error("Failed to create degree program", e);
    return ResponseEntity.badRequest()
        .body(new ApiError("Программа с таким названием уже существует"));
  }
}
