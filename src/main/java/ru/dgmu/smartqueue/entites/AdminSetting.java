package ru.dgmu.smartqueue.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import ru.dgmu.smartqueue.enums.AdminSettingResourceEnum;

@Entity
@Table(name = "admin_settings")
public class AdminSetting {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_setting_id_seq")
  @SequenceGenerator(name = "admin_setting_id_seq", sequenceName = "admin_setting_id_seq", allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "resource")
  private AdminSettingResourceEnum resource;

  @Column(name = "key")
  private String key;

  @Column(name = "value")
  private String value;
}
