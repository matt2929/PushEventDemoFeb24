package org.example.applications;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.rabbitmq.QueueClient;

import java.net.ConnectException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Pusher implements Application{

    private final static String QUEUE_NAME = "hello";

    public void run() throws Exception{
        try(QueueClient queueClient = new QueueClient()){
            queueClient.init();
        }
    }
}
