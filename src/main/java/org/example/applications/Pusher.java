package org.example.applications;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.net.ConnectException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Pusher implements Application{

    private final static String QUEUE_NAME = "hello";

    public void run() throws Exception{
        final Optional<Connection> connection = getConnection();
        System.out.println(connection.get().getPort());
        if (connection.isEmpty()) {
            System.out.println("Failed to establish connection, killing session!");
            throw new RuntimeException();
        }
        final Optional<Channel> channel = getChannelWithRetries(connection.get());

        if (channel.isEmpty()) {
            System.out.println("Failed to establish connection, killing session!");
            throw new RuntimeException();
        }
        String message = "Hello World!";
        channel.get().basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.get().close();
        connection.get().close();
    }


    private Optional<Connection> getConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        final int maxRetry = 100;
        final long sleepSeconds = TimeUnit.SECONDS.toMillis(5);
        int count = 0;
        while (count < maxRetry) {
            factory.setHost("rabbitmq");
            try {
                return Optional.of(factory.newConnection());
            } catch (ConnectException e) {
                System.out.println(String.format("Failed to connect retrying in %s", sleepSeconds));
                e.printStackTrace();
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
