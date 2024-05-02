package org.example.models.coupons;

import lombok.Builder;
import lombok.Getter;

import javax.money.MonetaryAmount;
import java.util.UUID;

@Builder
@Getter
public class Product {

    UUID uuid;
    String itemType;
    MonetaryAmount monetaryAmount;

}
