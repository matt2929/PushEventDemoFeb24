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
import org.example.models.coupons.Product;
import org.junit.jupiter.api.Test;

public class CouponApplicatorTests {


  @Test
  public void getCouponValue() {
    final List<Product> products = ImmutableList.of(
        Product.builder().itemType("foo").uuid(UUID.randomUUID())
            .monetaryAmount(6d).build(),
        Product.builder().itemType("foo").uuid(UUID.randomUUID())
            .monetaryAmount(1d).build(),
        Product.builder().itemType("foo").uuid(UUID.randomUUID())
            .monetaryAmount(4d).build(),
        Product.builder().itemType("foo").uuid(UUID.randomUUID())
            .monetaryAmount(2d).build(),
        Product.builder().itemType("foo2").uuid(UUID.randomUUID())
            .monetaryAmount(4d).build()
    );

    final CouponApplicator couponApplicator = CouponApplicator
        .builder()
        .priceModifier((sum) -> sum.getMonetaryAmount() / 2d)
        .productFilter(new FirstxProductFilter(
            new AttributeBasedProductFilter<>(Product::getItemType,
                "foo"), 3))
        .build();
    final Map<UUID, Double> modifiedMapping =
        couponApplicator.modifyTotalCost(products);
    assertThat(modifiedMapping.entrySet(), hasSize(equalTo(3)));
    assertThat(modifiedMapping.get(products.get(1).getUuid()),
        equalTo(.5d));
    assertThat(modifiedMapping.get(products.get(3).getUuid()), equalTo(1d));
    assertThat(modifiedMapping.get(products.get(2).getUuid()), equalTo(2d));
  }

}
