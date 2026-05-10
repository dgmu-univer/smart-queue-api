package ru.dgmu.smartqueue.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<UserContextPresentationDto> login(
      @RequestBody SignInRequestDto request,
      HttpServletRequest httpRequest
  ) {
    Authentication authentication;
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    HttpSession session;
    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.username(), request.password())
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
    UserDto userDto = UserDto.fromEntity(user);
    return ResponseEntity.ok(userDto.getUserContextPresentation());
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();
    response.setStatus(HttpServletResponse.SC_OK);
  }
}