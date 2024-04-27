package org.example.applications;

import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RABBITMQ_CONSTANTS;

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
                    final boolean success = queueClient.getMessage();
                    if (success) {
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
