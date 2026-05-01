package ru.dgmu.smartqueue.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import ru.dgmu.smartqueue.entity.User;

public record UserDto(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    String username,
    String password,
    String email,
    Boolean isEnabled,
    Collection<? extends GrantedAuthority> authorities
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
        user.getEmail(),
        user.isEnabled(),
        user.getAuthorities()
    );
  }

  public UserInfoDto getUserInfo() {
    return new UserInfoDto(
        getFio(),
        username,
        email
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

  public record UserInfoDto(
      String fio,
      String username,
      String email
  ) {
  }
}
