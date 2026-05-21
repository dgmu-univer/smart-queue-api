package ru.dgmu.smartqueue.services;

public interface MobileVerificationSenderService {

  void sendVerificationCode(String phone, String verificationCode);

}
