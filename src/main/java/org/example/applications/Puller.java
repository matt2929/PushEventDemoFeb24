package org.example.applications;

import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RABBITMQ_CONSTANTS;

import java.util.Optional;

public class Puller implements Application {
    public void run() throws Exception {

        try (QueueClient queueClient = QueueClient.builder()
                .userName(RABBITMQ_CONSTANTS.USERNAME)
                .password(RABBITMQ_CONSTANTS.PASSWORD)
                .hostname(RABBITMQ_CONSTANTS.HOSTNAME)
                .build()) {
            queueClient.init();
            queueClient.createQueue("test");
            int count = 0;
            while (count < 1) {
                try {
                    final Optional<String> success = queueClient.getMessage("test");
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
