package ru.dgmu.smartqueue.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.dgmu.smartqueue.entites.User;
import ru.dgmu.smartqueue.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class PinAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String pin = (String) auth.getCredentials();
        User user = userRepository.findByPin(pin)
            .orElseThrow(() -> new AuthorizationDeniedException("Access Denied"));

        return new PinAuthenticationToken(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> auth) {
        return PinAuthenticationToken.class.isAssignableFrom(auth);
    }
}
