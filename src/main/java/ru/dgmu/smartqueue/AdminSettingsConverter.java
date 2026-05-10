package ru.dgmu.smartqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import lombok.experimental.UtilityClass;
import ru.dgmu.smartqueue.dtos.PeriodSettingsDto;
import ru.dgmu.smartqueue.dtos.SlotSettingsDto;
import ru.dgmu.smartqueue.entites.AdminSetting;

@UtilityClass
public class AdminSettingsConverter {

  private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

  public static PeriodSettingsDto convertToPeriodSettingsDto(AdminSetting adminSettings)
      throws JsonProcessingException {
    return mapper.readValue(adminSettings.getSettings(), PeriodSettingsDto.class);
  }

  public static SlotSettingsDto convertToSlotSettingsDto(AdminSetting adminSettings)
      throws JsonProcessingException {
    return mapper.readValue(adminSettings.getSettings(), SlotSettingsDto.class);
  }

  public static List<LocalDate> convertToNonWorkingDaysSettingsDto(AdminSetting adminSettings)
      throws JsonProcessingException {
    return mapper.readValue(adminSettings.getSettings(), new TypeReference<>() {
    });
  }
}
