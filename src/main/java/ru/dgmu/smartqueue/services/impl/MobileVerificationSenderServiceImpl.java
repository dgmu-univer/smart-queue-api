package ru.dgmu.smartqueue.services.impl;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.dgmu.smartqueue.clients.MtsExolveClient;
import ru.dgmu.smartqueue.exceptions.NumberWasNotFoundException;
import ru.dgmu.smartqueue.exceptions.SmsSendingException;
import ru.dgmu.smartqueue.services.MobileVerificationSenderService;

@Service
@RequiredArgsConstructor
public class MobileVerificationSenderServiceImpl implements MobileVerificationSenderService {

  private static final String SMS_TEMPLATE = "PIN: %s для онлайн-записи в приёмную комиссию ДГМУ";
  private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  private final MtsExolveClient smsClient;

  @Override
  public void sendVerificationCode(String phone, String verificationCode) {
    try {
      var parsedPhone = phoneUtil.parse(phone, "RU");
      var phoneWithCountryCode =
          parsedPhone.getCountryCode() + "" + parsedPhone.getNationalNumber();
      smsClient.sendSms(SMS_TEMPLATE.formatted(verificationCode), phoneWithCountryCode,
          !isMegafonOrYota(phone));
    } catch (HttpClientErrorException.NotFound e) {
      throw new NumberWasNotFoundException();
    } catch (Exception e) {
      throw new SmsSendingException(e);
    }
  }

  public boolean isMegafonOrYota(String phone) {
    if (phone == null || phone.length() != 10) {
      return false;
    }

    int code = (phone.charAt(0) - '0') * 100 + (phone.charAt(1) - '0') * 10 + (phone.charAt(2) - '0');

    if (code >= 920 && code <= 939) {
      return true;
    }

    return switch (code) {
      case 902, 904, 908, 950, 951, 991, 995, 996, 999 -> true;
      default -> false;
    };
  }
}
