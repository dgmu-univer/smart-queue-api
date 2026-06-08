package ru.dgmu.smartqueue.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  @Query("""
        SELECT a FROM Appointment a
              JOIN FETCH a.slot s
              JOIN FETCH s.degreeProgram dp
        WHERE a.isVerified = true AND CAST(s.startTimeAt AS localdate) between :from AND :to
          AND dp.id = :degreeId
      """)
  List<Appointment> findAllByInterval(LocalDate from, LocalDate to, Long degreeId);

  @Query("""
      SELECT COUNT(a) FROM Appointment a
      JOIN a.slot s
      WHERE a.isVerified = true AND (CAST(:date AS localdate) IS NULL OR CAST(s.startTimeAt AS localdate) = :date)
      AND (:degreeProgramId IS NULL OR s.degreeProgram.id = :degreeProgramId)
      """)
  long countByDateAndDegreeProgramId(
      @Param("date") LocalDate date,
      @Param("degreeProgramId") Long degreeProgramId
  );

  boolean existsBySlot_DegreeProgram_IdAndPhoneAndIsVerifiedTrue(Long degreeId, String phone);

  @Modifying
  @Query("DELETE FROM Appointment a WHERE a.slot.degreeProgram.id = :degreeId AND a.phone = :phone")
  void deleteByDegreeIdAndPhone(@Param("degreeId") Long degreeId, @Param("phone") String phone);

  @Modifying
  @Query("DELETE FROM Appointment a WHERE a.isVerified = false AND a.requestedAt < :expireTime")
  int deleteExpiredUnverifiedAppointments(@Param("expireTime") LocalDateTime expireTime);

  Optional<Appointment> findByPhoneAndIsVerifiedTrueAndSlot_DegreeProgram_Id(String phone, Long degreeId);
}
