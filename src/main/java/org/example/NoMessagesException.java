package org.example;

public class NoMessagesException extends RuntimeException{

    public NoMessagesException(String queueName) {
        super(queueName);
        System.out.printf("Timeout Retrieving messages from %s%n", queueName);
    }
}
