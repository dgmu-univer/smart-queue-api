package ru.dgmu.smartqueue.services;

import ru.dgmu.smartqueue.dtos.SignInRequestDto;

public interface AuthenticationService {
    void signIn(SignInRequestDto signInRequest);
}