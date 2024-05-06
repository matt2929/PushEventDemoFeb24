package org.example.applications;

import java.util.Optional;
import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RabbitMqConstants;

public class Puller implements Application {
  public void run(String[] args) throws Exception {

    try (QueueClient queueClient = QueueClient.builder()
        .userName(RabbitMqConstants.USERNAME)
        .password(RabbitMqConstants.PASSWORD)
        .hostname(RabbitMqConstants.HOSTNAME)
        .build()) {
      queueClient.init();
      queueClient.createQueue("test");
      int count = 0;
      while (count < 1) {
        try {
          final Optional<String> success =
              queueClient.getMessage("test");
          if (success.isEmpty()) {
            count++;
          } else {
            System.out.println("Nothing yet...");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        Thread.sleep(1000);
      }
    }
  }
}
