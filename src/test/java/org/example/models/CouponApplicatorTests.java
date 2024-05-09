package org.example.models;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.example.models.coupons.AttributeBasedProductFilter;
import org.example.models.coupons.CouponApplicator;
import org.example.models.coupons.FirstxProductFilter;
import org.example.models.coupons.ProductEntity;
import org.junit.jupiter.api.Test;

public class CouponApplicatorTests {


  @Test
  public void getCouponValue() {
    final List<ProductEntity> productEntities = ImmutableList.of(
        ProductEntity.builder().itemType("foo").uuid(UUID.randomUUID().toString())
            .monetaryAmount(6d).build(),
        ProductEntity.builder().itemType("foo").uuid(UUID.randomUUID().toString())
            .monetaryAmount(1d).build(),
        ProductEntity.builder().itemType("foo").uuid(UUID.randomUUID().toString())
            .monetaryAmount(4d).build(),
        ProductEntity.builder().itemType("foo").uuid(UUID.randomUUID().toString())
            .monetaryAmount(2d).build(),
        ProductEntity.builder().itemType("foo2").uuid(UUID.randomUUID().toString())
            .monetaryAmount(4d).build()
    );

    final CouponApplicator couponApplicator = CouponApplicator
        .builder()
        .priceModifier((sum) -> sum.getMonetaryAmount() / 2d)
        .productFilter(new FirstxProductFilter(
            new AttributeBasedProductFilter<>(ProductEntity::getItemType,
                "foo"), 3))
        .build();
    final Map<String, Double> modifiedMapping =
        couponApplicator.modifyTotalCost(productEntities);
    assertThat(modifiedMapping.entrySet(), hasSize(equalTo(3)));
    assertThat(modifiedMapping.get(productEntities.get(1).getUuid()),
        equalTo(.5d));
    assertThat(modifiedMapping.get(productEntities.get(3).getUuid()), equalTo(1d));
    assertThat(modifiedMapping.get(productEntities.get(2).getUuid()), equalTo(2d));
  }

}
