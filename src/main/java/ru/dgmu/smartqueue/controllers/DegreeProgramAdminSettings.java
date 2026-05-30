package ru.dgmu.smartqueue.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.ExcludedSlotSettingsDto;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.services.AdminSettingService;

@RestController
@RequestMapping("/degree-programs/{degreeId}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Admin Settings", description = "Управление настройками администратора")
@SecurityRequirement(name = "bearerAuth")
public class DegreeProgramAdminSettings {

  private final AdminSettingService adminSettingService;

  @GetMapping("/admin-settings/periods")
  @Operation(summary = "Получить настройки периода", description = "Возвращает текущие настройки периода работы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки периода успешно получены",
          content = @Content(schema = @Schema(implementation = PeriodSettingsDto.class))),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
  public ResponseEntity<PeriodSettingsDto> getPeriodSettings(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId) {
    log.debug("Getting period settings");
    PeriodSettingsDto settings = adminSettingService.getPeriodSettings(degreeId);
    return ResponseEntity.ok(settings);
  }

  @PatchMapping("/admin-settings/periods")
  @Operation(summary = "Обновить настройки периода", description = "Обновляет настройки периода работы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки периода успешно обновлены"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> patchPeriodSettings(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId,
      @RequestBody PeriodSettingsDto periodSettingsDto) throws JsonProcessingException {
    adminSettingService.updatePeriodSettings(degreeId, periodSettingsDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/admin-settings/slots")
  @Operation(summary = "Получить настройки слотов", description = "Возвращает текущие настройки слотов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки слотов успешно получены",
          content = @Content(schema = @Schema(implementation = SlotSettingsDto.class))),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
  public ResponseEntity<SlotSettingsDto> getSlots(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId) {
    log.debug("Getting slot settings");
    SlotSettingsDto settings = adminSettingService.getSlotSettings(degreeId);
    return ResponseEntity.ok(settings);
  }

  @PatchMapping("/admin-settings/slots")
  @Operation(summary = "Обновить настройки слотов", description = "Обновляет настройки слотов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Настройки слотов успешно обновлены"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> patchSlotsSettings(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId,
      @RequestBody @Valid SlotSettingsDto updatedSlotSettingsDto) {
    log.info("Updating slot settings");
    adminSettingService.updateSlotSettings(degreeId, updatedSlotSettingsDto);
    log.info("Slot settings updated successfully");
    return ResponseEntity.ok().build();
  }

  @GetMapping("/admin-settings/non-working-days")
  @Operation(summary = "Получить нерабочие дни", description = "Возвращает список нерабочих дней")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список нерабочих дней успешно получен", content =  @Content(schema = @Schema(implementation = List.class))),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
  public ResponseEntity<List<LocalDate>> getNonWorkingDays(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId) {
    log.debug("Getting non-working days");
    List<LocalDate> nonWorkingDays = adminSettingService.getNonWorkingDays(degreeId);
    return ResponseEntity.ok(nonWorkingDays);
  }

  @PutMapping("/admin-settings/non-working-days")
  @Operation(summary = "Обновить нерабочие дни", description = "Обновляет список нерабочих дней")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список нерабочих дней успешно обновлен"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> putNonWorkingDays(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId,
      @RequestBody @Valid @Parameter(description = "Обновленный список нерабочих дней", required = true)
      List<LocalDate> updatedNonWorkingDays) {
    log.info("Updating non-working days, count: {}", updatedNonWorkingDays.size());
    adminSettingService.updateNonWorkingDays(degreeId, updatedNonWorkingDays);
    log.info("Non-working days updated successfully");
    return ResponseEntity.ok().build();
  }

  @GetMapping("/admin-settings/excluded-slots")
  @Operation(summary = "Получить исключенные слоты", description = "Возвращает список исключенных слотов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список исключенных слотов успешно получен"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
  public ResponseEntity<List<ExcludedSlotSettingsDto>> getExcludedSlots(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId) {
    log.debug("Getting excluded slots");
    return ResponseEntity.ok(adminSettingService.getExcludedSlots(degreeId));
  }

  @PostMapping("/admin-settings/excluded-slots")
  @Operation(summary = "Создать исключенный слот", description = "Создает новый исключенный слот")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Исключенный слот успешно создан"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> createExcludedSlot(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId,
      @RequestBody @Valid ExcludedSlotSettingsDto excludedSlotSettingsDto) {
    log.info("Creating excluded slot");
    adminSettingService.createExcludedSlot(degreeId, excludedSlotSettingsDto);
    log.info("Excluded slot created successfully");
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/admin-settings/excluded-slots/{id}")
  @Operation(summary = "Удалить исключенный слот", description = "Удаляет исключенный слот по ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Исключенный слот успешно удален"),
      @ApiResponse(responseCode = "404", description = "Исключенный слот не найден"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Нет прав доступа")
  })
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  public ResponseEntity<Void> deleteExcludedSlot(
      @Parameter(description = "Идентификатор программы образования") @PathVariable Long degreeId,
      @Parameter(description = "ID исключенного слота") @PathVariable Long id) {
    log.info("Deleting excluded slot with ID: {}", id);
    adminSettingService.deleteExcludedSlot(degreeId, id);
    log.info("Excluded slot deleted successfully with ID: {}", id);
    return ResponseEntity.ok().build();
  }
}
