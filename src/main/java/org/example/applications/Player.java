package org.example.applications;

import java.util.Optional;
import java.util.UUID;
import org.example.models.war.Card;
import org.example.models.war.PlayerDeck;
import org.example.rabbitmq.QueueClient;
import org.example.rabbitmq.RabbitMqConstants;

public class Player implements Application {

  public final UUID playerId = UUID.randomUUID();
  public final String playerQueueName = String.format("PLAYER:ID%s", playerId);
  public final String dealerQueueName = "DEALER";


  PlayerDeck playerDeck = new PlayerDeck();
  QueueClient queueClient;

  public void run(String[] args) throws Exception {
    try {
      queueClient = QueueClient.builder()
          .userName(RabbitMqConstants.USERNAME)
          .password(RabbitMqConstants.PASSWORD)
          .hostname(RabbitMqConstants.HOSTNAME)
          .build();
      queueClient.init();
      queueClient.createQueue(playerQueueName);
      queueClient.createQueue(dealerQueueName);
      registerPlayer();
      receiveRegistrationAck();
      receiveDeck();
      System.out.println("I got all cards I'm happy");
    } finally {
      queueClient.close();
    }
  }

  public void receiveRegistrationAck() {
    Optional<String> message =
        queueClient.getMessageWithRetries(playerQueueName);
    if (message.isEmpty()) {
      throw new RuntimeException();
    }
    final String registerKey = message.get().split(":")[0];
    if (!registerKey.equals("ACK")) {
      throw new RuntimeException(String.format("wtf is %s", registerKey));
    }
    System.out.println("i got an ACK! OwO");
  }

  public void registerPlayer() {
    System.out.printf("Sending Registration: %s%n", playerId);
    queueClient.sendMessage(dealerQueueName,
        String.format("HI:%s", playerId));
  }

  public void receiveDeck() {
    while (playerDeck.numCards() < 25000) {
      System.out.printf("waiting for more cards %d%n....",
          playerDeck.numCards());
      Optional<Card> card = checkForOffer();
      card.ifPresent(value -> playerDeck.receiveCard(value));
    }
  }

  public Optional<Card> checkForOffer() {
    return queueClient.getMessageWithRetries(playerQueueName)
        .map(Card::fromString);
  }

  public void receiveBattleResponse() {

  }
}


