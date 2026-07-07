package ru.dgmu.smartqueue.services.impl;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.dgmu.smartqueue.clients.MtsExolveClient;
import ru.dgmu.smartqueue.exceptions.NumberWasNotFoundException;
import ru.dgmu.smartqueue.exceptions.SmsSendingException;
import ru.dgmu.smartqueue.services.MobileVerificationSenderService;

@Service
@RequiredArgsConstructor
@Slf4j
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
      if (isMegafonOrYota(phone)) {
        smsClient.sendSms(SMS_TEMPLATE.formatted(verificationCode), phoneWithCountryCode, false);
      } else if (isMtsOrBeeline(phone)) {
        smsClient.sendSms(SMS_TEMPLATE.formatted(verificationCode), phoneWithCountryCode, true);
      }
    } catch (HttpClientErrorException.NotFound e) {
      throw new NumberWasNotFoundException();
    } catch (Exception e) {
      log.error("Ошибка отправки СМС", e);
    }
  }

  private boolean isMegafonOrYota(String phone) {
    int code = getCode(phone);

    if (code >= 920 && code <= 939) {
      return true;
    }

    return switch (code) {
      case 902, 904, 908, 950, 951, 991, 995, 996, 999 -> true;
      default -> false;
    };
  }

  private boolean isMtsOrBeeline(String phone) {
    int code = getCode(phone);

    if ((code >= 910 && code <= 919) ||
        (code >= 960 && code <= 969) ||
        (code >= 980 && code <= 989)) {
      return true;
    }

    return switch (code) {
      case 900, 901, 902, 903, 904, 905, 906, 908, 909, 950, 951, 953, 978 -> true;
      default -> false;
    };
  }

  private int getCode(String phone) {
    return (phone.charAt(0) - '0') * 100 + (phone.charAt(1) - '0') * 10 + (phone.charAt(2) - '0');
  }
}
