package ru.dgmu.smartqueue.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.controllers.SlotController.SlotFilter;
import ru.dgmu.smartqueue.entites.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

  @Query("""
          SELECT s FROM Slot s
          LEFT JOIN s.appointments a
          WHERE (COALESCE(:dateStart, NULL) IS NULL OR s.startTimeAt >= :dateStart)
            AND (COALESCE(:dateEnd, NULL) IS NULL OR s.endTimeAt <= :dateEnd)
            AND (COALESCE(:degreeId, NULL) IS NULL OR s.degreeProgram.id = :degreeId)
          GROUP BY s.id, s.degreeProgram.id, s.endTimeAt, s.startTimeAt
          HAVING (:isBooked = true  AND COUNT(a.id) < :slotCapacity)
              OR (:isBooked = false AND COUNT(a.id) >= :slotCapacity)
      """)
  List<Slot> findSlotsByFilter(
      @Param("dateStart") LocalDateTime dateStart,
      @Param("dateEnd") LocalDateTime dateEnd,
      @Param("degreeId") Long degreeId,
      @Param("isBooked") boolean isBooked,
      @Param("slotCapacity") int slotCapacity
  );
}