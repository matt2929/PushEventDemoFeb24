package org.example.applications;

import org.example.models.war.Card;
import org.example.models.war.PlayerDeck;
import org.example.models.war.Suit;
import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RABBITMQ_CONSTANTS;

import java.util.*;

import static org.example.models.war.Suit.*;

public class Dealer implements Application {


    public static final UUID PLAYER_ID = UUID.randomUUID();
    public static final String DEALER_QUEUE_NAME = "DEALER";

    public Map<String, Integer> scoring = new HashMap<>();

    QueueClient queueClient;

    public void run() throws Exception {
        queueClient = QueueClient.builder()
                .userName(RABBITMQ_CONSTANTS.USERNAME)
                .password(RABBITMQ_CONSTANTS.PASSWORD)
                .hostname(RABBITMQ_CONSTANTS.HOSTNAME)
                .build();
        queueClient.init();
        queueClient.createQueue(DEALER_QUEUE_NAME);
        receiveRegistrations();
        sendDeck();
        System.out.println("I sent all cards I'm happy");
    }

    public void receiveRegistrations() {
        while (scoring.size() < 2) {
            Optional<String> message = queueClient.getMessageWithRetries(DEALER_QUEUE_NAME);
            if (message.isEmpty()) {
                System.out.printf("still only got %d ", scoring.size());
                continue;
            }
            final String[] split = message.get().split(":");
            if (!split[0].equals("HI")) {
                throw new RuntimeException(String.format("WTF is %s", split[0]));
            }
            scoring.put(split[1], 0);
            System.out.printf("Received Player %s%n", split[1]);
            final String playerQueue = String.format("PLAYER:ID%s", split[1]);
            queueClient.sendMessage(playerQueue, String.format("ACK:%s", split[1]));
            System.out.printf("Sent Player %s an ACK%n", split[1]);
        }
    }

    public void waitForCards() {
        Map<String, Integer> currentCards = new HashMap<>();
        while (currentCards.size() < 2) {
            Optional<String> message = queueClient.getMessageWithRetries(DEALER_QUEUE_NAME);

        }
    }

    public void sendDeck() {

        List<Card> cards = new LinkedList<>();
        for (Suit suit : Arrays.asList(HEARTS, CLUBS, SPADES, DIAMONDS)) {
            for (int i = 0; i < 50000; i++) {
                cards.add(Card.builder().suit(suit).value(i).build());
            }
        }
        System.out.printf("Sending %d cards%n", cards.size());
        Collections.shuffle(cards);
        final Queue<Card> queueCards = (Queue) cards;
        while (!queueCards.isEmpty()) {
            for (String player : scoring.keySet()) {
                final String playerQueue = String.format("PLAYER:ID%s", player);
                queueClient.sendMessage(playerQueue, queueCards.poll().toString());
            }
            System.out.printf("%d More too send...%n", queueCards.size());
        }
    }


}