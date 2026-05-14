package ru.dgmu.smartqueue.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "degree_programs")
public class DegreeProgram {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_id_seq")
  @SequenceGenerator(name = "slot_id_seq", sequenceName = "slot_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @JoinColumn(name = "user_id")
  @OneToOne(cascade = CascadeType.ALL)
  private User userId;
}
