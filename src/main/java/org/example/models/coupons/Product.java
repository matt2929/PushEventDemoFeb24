package org.example.models.coupons;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {

  UUID uuid;
  String itemType;
  Double monetaryAmount;

}
