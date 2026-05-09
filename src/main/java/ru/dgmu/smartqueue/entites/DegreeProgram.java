package ru.dgmu.smartqueue.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "degree_programs")
public class DegreeProgram {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "degree_program_id_seq")
  @SequenceGenerator(name = "degree_program_id_seq", sequenceName = "degree_program_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "pin")
  private String pin;

  @Column(name = "description")
  private String description;
}
