package ru.dgmu.smartqueue.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "slots")
public class Slot {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_id_seq")
  @SequenceGenerator(name = "slot_id_seq", sequenceName = "slot_id_seq", allocationSize = 1)
  private Long id;

  @JoinColumn(name = "degree_program_id")
  @ManyToOne(cascade = CascadeType.ALL)
  private DegreeProgram degreeProgram;

  @Column(name = "start_time_at")
  private LocalDateTime startTimeAt;

  @Column(name = "end_time_at")
  private LocalDateTime endTimeAt;

  @Column(name = "availiable_count")
  private Integer availableCount;
}
