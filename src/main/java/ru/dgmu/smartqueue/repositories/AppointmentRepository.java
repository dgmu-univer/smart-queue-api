package ru.dgmu.smartqueue.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

//  @Query("""
//    SELECT a FROM Appointment a
//    WHERE a.booked = :#{#filter.booked}
//      AND a.date = :#{#filter.date}
//      AND a.degreeId = :#{#filter.degreeId}
//  """)
//  List<Appointment> findByFilter(@Param("filter") AppointmentsFilter filter);
}
