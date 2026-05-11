package ru.dgmu.smartqueue.entites;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @JoinColumn(name = "user_id")
  @OneToOne(cascade = CascadeType.ALL)
  private User userId;
}
