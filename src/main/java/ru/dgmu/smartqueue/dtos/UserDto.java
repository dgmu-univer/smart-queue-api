package ru.dgmu.smartqueue.dtos;

import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.enums.Role;

public record UserDto(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    String username,
    String password,
    Boolean isEnabled,
    Role role
) {

  private static final String FULL_FIO_FORMAT = "%s %s %s";
  private static final String PART_FIO_FORMAT = "%s %s";

  public static UserDto fromEntity(User user) {
    return new UserDto(
        user.getId(),
        user.getFirstName(),
        user.getMiddleName(),
        user.getLastName(),
        user.getUsername(),
        user.getPassword(),
        user.isEnabled(),
        user.getRole()
    );
  }

  public UserContextPresentationDto getUserContextPresentation() {
    return new UserContextPresentationDto(
        getFio(),
        username,
        role.name()
    );
  }

  private String getFio() {
    return isFullFioAvailable()
        ? String.format(FULL_FIO_FORMAT, firstName, middleName, lastName)
        : String.format(PART_FIO_FORMAT, firstName, middleName);
  }

  private boolean isFullFioAvailable() {
    return lastName != null;
  }

  public record UserContextPresentationDto(
      String fio,
      String username,
      String role
  ) {
  }
}
