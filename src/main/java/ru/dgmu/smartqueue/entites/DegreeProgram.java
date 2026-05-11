package ru.dgmu.smartqueue.entites;

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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "degree_program_id_seq")
  @SequenceGenerator(name = "degree_program_id_seq", sequenceName = "degree_program_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @JoinColumn(name = "user_id")
  @OneToOne
  private User userId;
}
