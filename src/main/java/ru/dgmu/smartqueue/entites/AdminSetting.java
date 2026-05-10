package ru.dgmu.smartqueue.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.dgmu.smartqueue.enums.AdminSettingResourceEnum;

@Data
@Entity
@Table(name = "admin_settings")
public class AdminSetting {

  @Id
  @Enumerated(EnumType.STRING)
  @Column(name = "resource")
  private AdminSettingResourceEnum resource;

  @Column(name = "settings")
  @JdbcTypeCode(SqlTypes.JSON)
  private String settings;
}
