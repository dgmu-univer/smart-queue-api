package ru.dgmu.smartqueue.services.impl;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class OneTimeTokenGenerator {

  public static VerificationCode generateCode() {
    return new VerificationCode(RandomStringUtils.secure().nextNumeric(6));
  }

  public record VerificationCode(
      String value

  ) {

    public String formattedValue() {
      return value.substring(0, 3) + " " + value.substring(3);
    }
  }
}