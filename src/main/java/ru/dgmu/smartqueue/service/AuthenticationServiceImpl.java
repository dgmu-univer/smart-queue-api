//package ru.dgmu.smartqueue.service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import ru.dgmu.smartqueue.dto.SignInRequestDto;
//
//@Service
//@AllArgsConstructor
//public class AuthenticationServiceImpl implements AuthenticationService {
//
//    private final UserService userService;
//    private final AuthenticationManager authenticationManager;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void signIn(SignInRequestDto signInRequest) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                signInRequest.getUsername(),
//                signInRequest.getPassword()
//        ));
//
//        var user = userService
//                .userDetailsService()
//                .loadUserByUsername(signInRequest.getUsername());
//
////        var token = jwtTokenService.generateToken(user);
////        return JwtAuthenticationResponseDto.builder()
////                .token(token)
////                .build();
//    }
//}
