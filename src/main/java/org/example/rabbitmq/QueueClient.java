package org.example.rabbitmq;


import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import com.rabbitmq.client.*;
import jdk.jshell.spi.ExecutionControl;
import lombok.Builder;
import org.example.NoMessagesException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;


public class QueueClient implements AutoCloseable {

    private Optional<Connection> connection;
    private Optional<Channel> channel;

    private String userName;
    private String password;
    private String hostname;

    @Builder
    public QueueClient(final String userName,
                       final String password,
                       final String hostname) {
        this.userName = userName;
        this.password = password;
        this.hostname = hostname;
        this.connection = Optional.empty();
        this.channel = Optional.empty();
    }


    public void init() {
        Callable<Connection> establishConnection = () -> {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(userName);
            factory.setPassword(password);
            factory.setHost(hostname);
            System.out.println("Trying to establish connection...");
            return factory.newConnection();
        };
        Status<Connection> connectionResult = new CallExecutorBuilder().config(getDefaultConfig()).build().execute(establishConnection);
        this.connection = Optional.ofNullable(connectionResult.getResult());
        if (this.connection.isEmpty()) {
            throw new RuntimeException("Failure to establish connection");
        }
        Callable<Channel> establishChannel = () -> {
            System.out.println("Trying to establish channel...");
            return this.connection.get().createChannel();
        };
        Status<Channel> channelResult = new CallExecutorBuilder().config(getDefaultConfig()).build().execute(establishChannel);
        this.channel = Optional.ofNullable(channelResult.getResult());
        if (this.channel.isEmpty()) {
            throw new RuntimeException("Failure to establish channel");
        }
    }

    public boolean createQueue(final String name) {
        if (this.channel.isEmpty()) {
            return false;
        }
        try {
            this.channel.get().queueDeclare(name, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendMessage(final String queueName, final String message) {
        if (this.channel.isEmpty()) {
            return false;
        }
        try {
            this.channel.get().basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Optional<String> getMessage(final String queueName) {
        System.out.printf("Trying to pull message from %s%n", queueName);
        if (channel.isEmpty()){
            System.out.println("There's no channel! Im gonna die....");
            return Optional.empty();
        }
        try {
            final Optional<GetResponse> response = Optional.ofNullable(channel.get().basicGet(queueName, true));
            if(response.isEmpty()){
                throw new NoMessagesException(queueName);
            }
            return response.map(getResponse -> new String(getResponse.getBody(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<String> getMessageWithRetries(final String queueName) {
        if (channel.isEmpty()){
            throw new RuntimeException("No Channel");
        }
        Callable<Optional<String>> retrieveMessage = () -> getMessage(queueName);
        Status<Optional<String>> connectionResult = new CallExecutorBuilder().config(getDefaultConfig()).build().execute(retrieveMessage);
        return connectionResult.getResult();
    }

    @Override
    public void close() throws Exception {
        if (channel.isPresent()) {
            channel.get().close();
        }
        if (connection.isPresent()) {
            connection.get().close();
        }
        System.out.println("Successfully killed queue client...");
    }

    private RetryConfig getDefaultConfig() {
        return new RetryConfigBuilder().withMaxNumberOfTries(5)
                .withFixedBackoff().withDelayBetweenTries(5, SECONDS)
                .retryOnSpecificExceptions(
                        RuntimeException.class,
                        UnknownHostException.class,
                        ConnectException.class,
                        NoMessagesException.class
                )
                .build();
    }
}






