package ru.dgmu.smartqueue.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.controllers.SlotController;
import ru.dgmu.smartqueue.entites.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

  @Query("""
    SELECT s FROM Slot s
    LEFT JOIN s.appointments a
    WHERE s.startTimeAt >= :#{#filter.date.atStartOfDay()}
      AND s.endTimeAt <= :#{#filter.date.atTime(23, 59, 59)}
      AND s.degreeProgram.id = :#{#filter.degreeId}
    GROUP BY s
    HAVING COUNT(a) < 10
""")
  List<Slot> findByFilterBookedSlots(@Param("filter") SlotController.SlotFilter filter, int slotCapacity);

  @Query("""
    SELECT s FROM Slot s
    LEFT JOIN s.appointments a
    WHERE s.startTimeAt >= :#{#filter.date.atStartOfDay()}
      AND s.endTimeAt <= :#{#filter.date.atTime(23, 59, 59)}
      AND s.degreeProgram.id = :#{#filter.degreeId}
    GROUP BY s
    HAVING COUNT(a) >= 10
""")
  List<Slot> findByFilterNotBookedSlots(@Param("filter") SlotController.SlotFilter filter, int slotCapacity);

}
