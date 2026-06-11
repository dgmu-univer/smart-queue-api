package ru.dgmu.smartqueue.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MtsExolveClient {

  @Value("${app.sms.apiKey}")
  private String apiKey;

  @Value("${app.sms.url}")
  private String url;

  @Value("${app.sms.number}")
  private String senderNumber;

  private final RestClient restClient;

  public MtsExolveClient() {
    this.restClient = RestClient.create();
  }

  public void sendSms(String message, String destinationNumber) {
//    restClient.post()
//        .uri(url)
//        .contentType(MediaType.APPLICATION_JSON)
//        .header("Authorization", "Bearer %s".formatted(apiKey))
//        .body(new RequestBody(senderNumber, destinationNumber, message))
//        .retrieve()
//        .toBodilessEntity();
  }

  record RequestBody(
      String number,
      String destination,
      String text
  ) {

  }
}
