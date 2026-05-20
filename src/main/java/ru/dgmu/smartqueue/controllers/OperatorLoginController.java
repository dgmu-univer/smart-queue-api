package ru.dgmu.smartqueue.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dgmu.smartqueue.configs.PinAuthenticationToken;
import ru.dgmu.smartqueue.dtos.OperatorLoginSuccessDto;
import ru.dgmu.smartqueue.dtos.SignInOperatorDto;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.exception.AuthenticationFailedException;
import ru.dgmu.smartqueue.services.OperatorLoginService;

@RestController
@Tag(name = "Operator login", description = "Аутентификация оператора")
@RequiredArgsConstructor
@Slf4j
public class OperatorLoginController {

  private final AuthenticationManager authenticationManager;
  private final OperatorLoginService operatorLoginService;

  @PostMapping("/operator/login")
  @Operation(description = "Аутентификация оператора")
  public ResponseEntity<OperatorLoginSuccessDto> login(
      @RequestBody @Valid SignInOperatorDto request,
      HttpServletRequest httpRequest
  ) {
    try {
      log.debug("Attempting operator authentication with PIN");

      Authentication authentication = authenticationManager.authenticate(
          new PinAuthenticationToken(request.pin())
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
      OperatorLoginSuccessDto loginResult = operatorLoginService.login(user);

      log.info("Operator {} successfully authenticated", user.getUsername());
      return ResponseEntity.ok(loginResult);

    } catch (AuthenticationException e) {
      log.warn("Operator authentication failed with PIN");
      SecurityContextHolder.clearContext();
      throw new AuthenticationFailedException("Неверный PIN-код");
    } catch (Exception e) {
      log.error("Unexpected error during operator authentication", e);
      SecurityContextHolder.clearContext();
      throw new RuntimeException("Ошибка при аутентификации оператора");
    }
  }
}