package org.example.applications;

import org.example.models.war.Card;
import org.example.models.war.PlayerDeck;
import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RABBITMQ_CONSTANTS;

import java.util.Optional;
import java.util.UUID;

public class Player implements Application {

    public final UUID PLAYER_ID = UUID.randomUUID();
    public final String PLAYER_QUEUE_NAME = String.format("PLAYER:ID%s", PLAYER_ID);
    public final String DEALER_QUEUE_NAME = "DEALER";


    PlayerDeck playerDeck = new PlayerDeck();
    QueueClient queueClient;

    public void run() throws Exception {
        try {
            queueClient = QueueClient.builder()
                    .userName(RABBITMQ_CONSTANTS.USERNAME)
                    .password(RABBITMQ_CONSTANTS.PASSWORD)
                    .hostname(RABBITMQ_CONSTANTS.HOSTNAME)
                    .build();
            queueClient.init();
            queueClient.createQueue(PLAYER_QUEUE_NAME);
            queueClient.createQueue(DEALER_QUEUE_NAME);
            registerPlayer();
            receiveRegistrationACK();
            receiveDeck();
            System.out.println("I got all cards I'm happy");
        } finally {
            queueClient.close();
        }
    }
    public void receiveRegistrationACK() {
        Optional<String> message = queueClient.getMessageWithRetries(PLAYER_QUEUE_NAME);
        if(message.isEmpty()){
            throw new RuntimeException();
        }
        final String registerKey = message.get().split(":")[0];
        if(!registerKey.equals("ACK")){
            throw new RuntimeException(String.format("wtf is %s", registerKey));
        }
        System.out.println("i got an ACK! OwO");
    }
    public void registerPlayer() {
        System.out.printf("Sending Registration: %s%n",PLAYER_ID);
        queueClient.sendMessage(DEALER_QUEUE_NAME, String.format("HI:%s", PLAYER_ID));
    }

    public void receiveDeck() {
        while(playerDeck.numCards()<25000){
            System.out.printf("waiting for more cards %d%n....", playerDeck.numCards());
            Optional<Card> card = checkForOffer();
            card.ifPresent(value -> playerDeck.receiveCard(value));
        }
    }

    public Optional<Card> checkForOffer(){
        return  queueClient.getMessageWithRetries(PLAYER_QUEUE_NAME).map(Card::fromString);
    }

    public void receiveBattleResponse() {

    }
}


