package ru.dgmu.smartqueue.service;

import ru.dgmu.smartqueue.dto.SignInRequestDto;

public interface AuthenticationService {
    void signIn(SignInRequestDto signInRequest);
}