package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.dtos.SignInRequestDto;
import ru.dgmu.smartqueue.dtos.UserDto;
import ru.dgmu.smartqueue.dtos.UserDto.UserContextPresentationDto;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.exceptions.AuthenticationFailedException;

@RestController
@Tag(name = "Admin login", description = "Аутентификация администратора")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  @Operation(description = "Аутентификация администратора")
  public ResponseEntity<UserContextPresentationDto> login(
      @RequestBody @Valid SignInRequestDto request,
      HttpServletRequest httpRequest
  ) {
    try {
      log.debug("Attempting authentication for user: {}", request.username());

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.username(), request.password())
      );

      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);

      HttpSession session = httpRequest.getSession(true);
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          context
      );

      User user = (User) authentication.getPrincipal();
      UserDto userDto = UserDto.fromEntity(user);

      log.info("User {} successfully authenticated", request.username());
      return ResponseEntity.ok(userDto.getUserContextPresentation());

    } catch (AuthenticationException e) {
      log.warn("Authentication failed for user: {}", request.username());
      SecurityContextHolder.clearContext();
      throw new AuthenticationFailedException("Неверное имя пользователя или пароль");
    }
  }

  @PostMapping("/logout")
  @Operation(description = "Выход администратора")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    try {
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.invalidate();
        log.debug("Session invalidated");
      }
      SecurityContextHolder.clearContext();
      log.debug("Security context cleared");

      return ResponseEntity.ok().build();
    } catch (Exception e) {
      log.error("Error during logout", e);
      SecurityContextHolder.clearContext();
      return ResponseEntity.ok().build();
    }
  }
}