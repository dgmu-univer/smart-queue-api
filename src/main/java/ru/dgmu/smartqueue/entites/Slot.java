package ru.dgmu.smartqueue.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "slots")
public class Slot {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_seq_gen")
  @SequenceGenerator(name = "slot_seq_gen", sequenceName = "slot_id_seq", allocationSize = 100)
  private Long id;

  @JoinColumn(name = "degree_program_id")
  @ManyToOne(cascade = CascadeType.ALL)
  private DegreeProgram degreeProgram;

  @Column(name = "start_time_at")
  private LocalDateTime startTimeAt;

  @Column(name = "end_time_at")
  private LocalDateTime endTimeAt;

  @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
  @JsonIgnore
  List<Appointment> appointments;
}
