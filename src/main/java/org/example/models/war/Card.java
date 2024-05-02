package org.example.models.war;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Card {

    Suit suit;
    int value;

    public static Card fromString(String str) {
        String[] split = str.split(":");
        Suit suit = Suit.valueOf(split[0]);
        int value = Integer.parseInt(split[1]);
        return new Card(suit, value);
    }

    public String toString() {
        return String.format("%s:%d", suit.name(), value);
    }

}
