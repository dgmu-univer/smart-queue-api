package ru.dgmu.smartqueue.services.impl;

import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.services.MobileVerificationSenderService;

@Service
public class MobileVerificationSenderServiceImpl implements MobileVerificationSenderService {

  @Override
  public void sendVerificationCode(String phone, String verificationCode) {
    // send sms
  }
}
