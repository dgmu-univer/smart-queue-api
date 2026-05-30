package ru.dgmu.smartqueue.services.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dgmu.smartqueue.clients.MtsExolveClient;
import ru.dgmu.smartqueue.exception.SmsSendingException;
import ru.dgmu.smartqueue.services.MobileVerificationSenderService;

@Service
@RequiredArgsConstructor
public class MobileVerificationSenderServiceImpl implements MobileVerificationSenderService {

  private static final String SMS_TEMPLATE = "Ваш код подтверждения: %s";
  private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  private final MtsExolveClient smsClient;

  @Override
  public void sendVerificationCode(String phone, String verificationCode) {
    try {
      var parsedPhone = phoneUtil.parse(phone, "RU");
      String phoneWithCountryCode = parsedPhone.getCountryCode() + "" + parsedPhone.getNationalNumber();
      smsClient.sendSms(SMS_TEMPLATE.formatted(verificationCode), phoneWithCountryCode);
    } catch (NumberParseException e) {
      throw new SmsSendingException(e);
    }
  }

  // todo код подверждения нужен для подтверждения записи. А pin как отдавать?
}
