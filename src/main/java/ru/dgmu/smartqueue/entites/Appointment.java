package ru.dgmu.smartqueue.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointments_id_gen_seq")
  @SequenceGenerator(name = "appointments_id_gen_seq", sequenceName = "appointments_id_seq", allocationSize = 1)
  private Long id;

  @Column
  private String pin;

  @Column
  private String phone;

  @Column
  private Boolean isVerified;

  @Column
  private LocalDateTime requestedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slot_id", nullable = false)
  private Slot slot;
}
