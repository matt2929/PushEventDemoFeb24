package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Time;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Pusher {

    private final static String QUEUE_NAME = "hello";

    public void push() throws IOException {
        final Optional<Connection> connection = getConnection();
        if (connection.isEmpty()) {
            System.out.println("Failed to establish connection, killing session!");
            throw new RuntimeException();
        }
        final Optional<Channel> channel = getChannelWithRetries(connection.get());
        if (channel.isEmpty()) {
            System.out.println("Failed to establish connection, killing session!");
            throw new RuntimeException();
        }
        channel.get().queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.get().basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

    }


    private Optional<Connection> getConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        final int maxRetry = 100;
        final long sleepSeconds = TimeUnit.SECONDS.toMillis(5);
        int count = 0;
        while (count < maxRetry) {
            factory.setHost("localhost");
            try {
                return Optional.of(factory.newConnection());
            } catch (ConnectException e) {
                System.out.println(String.format("Failed to connect retrying in %s", sleepSeconds));
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
            try {
                Thread.sleep(sleepSeconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }


    private Optional<Channel> getChannelWithRetries(final Connection connection) {
        ConnectionFactory factory = new ConnectionFactory();
        final int maxRetry = 5;
        final long sleepSeconds = TimeUnit.SECONDS.toMillis(5);
        int count = 0;
        while (count < maxRetry) {
            factory.setHost("localhost");
            try {
                return Optional.of(connection.createChannel());
            } catch (ConnectException e) {
                System.out.println(String.format("Failed to connect retrying in %s", sleepSeconds));
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
            try {
                Thread.sleep(sleepSeconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

}
