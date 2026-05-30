package ru.dgmu.smartqueue.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.AdminSetting;

@Repository
public interface AdminSettingsRepository extends JpaRepository<AdminSetting, Long> {

  Optional<AdminSetting> findByDegreeProgramId(Long degreeId);
}
