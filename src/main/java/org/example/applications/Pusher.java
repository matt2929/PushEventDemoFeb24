package org.example.applications;

import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RabbitMqConstants;

public class Pusher implements Application {


  public void run(String[] args) throws Exception {

    try (QueueClient queueClient = QueueClient.builder()
        .userName(RabbitMqConstants.USERNAME)
        .password(RabbitMqConstants.PASSWORD)
        .hostname(RabbitMqConstants.HOSTNAME)
        .build()) {
      queueClient.init();
      queueClient.createQueue("test");
      for (int i = 0; i < 5; i++) {
        queueClient.sendMessage("test", "" + i);
      }
    }
  }
}
