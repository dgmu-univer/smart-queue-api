package ru.dgmu.smartqueue.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
import ru.dgmu.smartqueue.dtos.DegreeProgramDto.DegreeProgramPresentation;
import ru.dgmu.smartqueue.dtos.SignInOperatorDto;
import ru.dgmu.smartqueue.entites.User;

@RestController
@RequiredArgsConstructor
public class OperatorLoginController {

  private final AuthenticationManager authenticationManager;
  private final DegreeProgramService degreeProgramService;

  @PostMapping("/operator/login")
  public ResponseEntity<DegreeProgramPresentation> login(
      @RequestBody SignInOperatorDto request,
      HttpServletRequest httpRequest
  ) {
    Authentication authentication;
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    HttpSession session;
    try {
      authentication = authenticationManager.authenticate(
          new PinAuthenticationToken(request.pin())
      );
    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();
      return ResponseEntity.badRequest().build();
    }

    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    session = httpRequest.getSession(true);
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        context
    );

    User user = (User) authentication.getPrincipal();

    var degreeProgramByUserId = degreeProgramService.findDegreeProgramByUserId(user);

    return ResponseEntity.ok(degreeProgramByUserId.toPresentation());
  }
}