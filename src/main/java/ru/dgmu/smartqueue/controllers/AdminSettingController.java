package ru.dgmu.smartqueue.controllers;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    return  ResponseEntity.ok(adminSettingService.getPeriodSettings());
  }

  @PatchMapping("/periods")
  public ResponseEntity<PeriodSettingsDto> patchPeriodSettings(PeriodSettingsDto periodSettingsDto) {
    return ResponseEntity.ok(adminSettingService.updatePeriodSettings(periodSettingsDto));
  }

  @GetMapping("/slots")
  public ResponseEntity<SlotSettingsDto> getSlots() {
    return ResponseEntity.ok(adminSettingService.getSlotSettings());
  }

  @PatchMapping("/slots")
  public ResponseEntity<SlotSettingsDto> patchSlotsSettings() {
    return ResponseEntity.ok(adminSettingService.updateSlotSettings());
  }


  @GetMapping("/non-working-days")
  public ResponseEntity<List<LocalDate>> getNonWorkingDays() {
    return ResponseEntity.ok(adminSettingService.getNonWorkingDays());
  }

  @PutMapping("/non-working-days")
  public  ResponseEntity<List<LocalDate>> putNonWorkingDays() {
    return ResponseEntity.ok(adminSettingService.updateNonWorkingDays());
  }

}
