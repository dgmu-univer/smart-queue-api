package ru.dgmu.smartqueue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dgmu.smartqueue.entites.AdminSetting;
import ru.dgmu.smartqueue.enums.AdminSettingResourceEnum;

@Repository
public interface AdminSettingsRepository extends
    JpaRepository<AdminSetting, AdminSettingResourceEnum> {

//  List<AdminSetting> findByResource(AdminSettingResourceEnum resource);
//  List<>
}
