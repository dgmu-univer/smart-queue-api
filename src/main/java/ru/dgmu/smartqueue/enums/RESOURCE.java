package ru.dgmu.smartqueue.enums;

import lombok.Getter;

@Getter
public enum RESOURCE {
  ADMIN_SETTINGS("Админские настройки"),
  DEGREE_PROGRAM("Программа образования"),
  SLOT("Слот"),
  APPOINTMENT("Запись в слот"),
  EXCLUDED_SLOT("Исключительный слот");

  private final String displayName;

  RESOURCE(String name) {
    displayName = name;
  }
}
