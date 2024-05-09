package org.example.models.coupons;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Builder
@Getter
public class ProductEntity {

  @BsonProperty("uuid")  public final String uuid;
  @BsonProperty("itemType")  public final String itemType;
  @BsonProperty("monetaryAmount")  public final Double monetaryAmount;

  @Builder
  @BsonCreator
  public ProductEntity(
      @BsonProperty("uuid") String uuid,
      @BsonProperty("itemType") String itemType,
      @BsonProperty("monetaryAmount") Double monetaryAmount) {
    this.uuid = uuid;
    this.itemType = itemType;
    this.monetaryAmount = monetaryAmount;
  }

}
