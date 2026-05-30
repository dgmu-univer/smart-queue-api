package ru.dgmu.smartqueue.entites;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_settings")
public class AdminSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "slot_duration_minutes", nullable = false)
  private Integer durationMinutes;

  @Column(name = "slot_capacity_per_slot", nullable = false)
  private Integer capacityPerSlot;

  @Column(name = "work_start_date", nullable = false)
  private LocalDate workStartDate;

  @Column(name = "work_end_date", nullable = false)
  private LocalDate workEndDate;

  @Column(name = "work_start_time", nullable = false)
  private LocalTime workStartTime;

  @Column(name = "work_end_time", nullable = false)
  private LocalTime workEndTime;

  @Column(name = "lunch_start_time")
  private LocalTime lunchStartTime;

  @Column(name = "lunch_end_time")
  private LocalTime lunchEndTime;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "non_working_days", joinColumns = @JoinColumn(name = "setting_id"))
  @Column(name = "non_working_date")
  private List<LocalDate> nonWorkingDays = new ArrayList<>();

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "excluded_slots",
      joinColumns = @JoinColumn(name = "setting_id")
  )
  private List<ExcludedSlot> excludedSlots = new ArrayList<>();

  @OneToOne(mappedBy = "adminSettings")
  private DegreeProgram degreeProgram;

}
