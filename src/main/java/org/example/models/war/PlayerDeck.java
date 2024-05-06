package org.example.models.war;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerDeck {

  Queue<Card> cards = new LinkedList<>();


  public Card draw() {
    return cards.poll();
  }

  public void receiveCard(final Card card) {
    cards.add(card);
  }

  public int numCards() {
    return cards.size();
  }

}
