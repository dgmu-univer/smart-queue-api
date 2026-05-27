package ru.dgmu.smartqueue.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  @Query("""
        SELECT a FROM Appointment a
        WHERE a.isVerified = true AND CAST(a.slot.startTimeAt AS localdate) between :from AND :to
          AND a.slot.degreeProgram.id = :degreeId
      """)
  List<Appointment> findAllByInterval(LocalDate from, LocalDate to, Long degreeId);

  @Query("SELECT COUNT(a) FROM Appointment a " +
      "JOIN a.slot s " +
      "WHERE (CAST(:date AS localdate) IS NULL OR CAST(s.startTimeAt AS localdate) = :date) " +
      "AND s.degreeProgram.id = :degreeProgramId")
  long countByDateAndDegreeProgramId(
      @Param("date") LocalDate date,
      @Param("degreeProgramId") Long degreeProgramId
  );


}
