package ru.dgmu.smartqueue.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "degree_programs_id_gen_seq")
  @SequenceGenerator(name = "degree_programs_id_gen_seq", sequenceName = "degree_programs_id_seq", allocationSize = 100)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @JoinColumn(name = "user_id")
  @OneToOne(cascade = CascadeType.ALL)
  @JsonIgnore
  private User userId;
}
