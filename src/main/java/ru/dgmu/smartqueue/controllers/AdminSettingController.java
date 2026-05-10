package ru.dgmu.smartqueue.controllers;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/admin-settings")
@RequiredArgsConstructor
public class AdminSettingController {

  private final AdminSettingService adminSettingService;

  // todo потом переделать под response если нужно

  @GetMapping("/periods")
  public ResponseEntity<PeriodSettingsDto> getPeriodSettings() {
    return ResponseEntity.ok(adminSettingService.getPeriodSettings());
  }

  @PatchMapping("/periods")
  public ResponseEntity<Void> patchPeriodSettings(
      @RequestBody PeriodSettingsDto updatedPeriodSettingsDto) {
    adminSettingService.updatePeriodSettings(updatedPeriodSettingsDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/slots")
  public ResponseEntity<SlotSettingsDto> getSlots() {
    return ResponseEntity.ok(adminSettingService.getSlotSettings());
  }

  @PatchMapping("/slots")
  public ResponseEntity<Void> patchSlotsSettings(
      @RequestBody SlotSettingsDto updatedSlotSettingsDto) {
    adminSettingService.updateSlotSettings(updatedSlotSettingsDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/non-working-days")
  public ResponseEntity<List<LocalDate>> getNonWorkingDays() {
    return ResponseEntity.ok(adminSettingService.getNonWorkingDays());
  }

  @PutMapping("/non-working-days")
  public ResponseEntity<List<LocalDate>> putNonWorkingDays(@RequestBody List<LocalDate> updatedNonWorkingDays) {
    return ResponseEntity.ok(adminSettingService.updateNonWorkingDays(updatedNonWorkingDays));
  }

  @GetMapping("/excluede-slots")
  public ResponseEntity<List<ExcludedSlotSettingsDto>> getExcluedeSlots() {
    return ResponseEntity.ok(adminSettingService.getExcludedSlots());
  }

  @PostMapping("/excluede-slots")
  public ResponseEntity<Void> createExcludeSlot(
      @RequestBody ExcludedSlotSettingsDto excludedSlotSettingsDto) {
    adminSettingService.createExcludedSlot(excludedSlotSettingsDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/excluede-slots/{id}")
  public ResponseEntity<Void> getExcluedeSlots(@PathVariable Long id) {
    adminSettingService.deleteExcludedSlot(id);
    return ResponseEntity.ok().build();
  }
}
