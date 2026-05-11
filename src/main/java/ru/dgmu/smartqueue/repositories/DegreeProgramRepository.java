package ru.dgmu.smartqueue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.DegreeProgram;

@Repository
public interface DegreeProgramRepository extends JpaRepository<DegreeProgram, Long> {

}
