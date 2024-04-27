package org.example.models;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import org.checkerframework.checker.units.qual.C;
import org.example.models.coupons.CouponApplicator;
import org.example.models.coupons.productfilters.ProductAttributeFilter;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CouponApplicatorTests {


    @Test
    public void getCouponValue() {

        final MonetaryAmount monetaryAmount = Monetary.getDefaultAmountFactory()
                .setCurrency("USD")
                .setNumber(1)
                .create();

        final List<Product> products = ImmutableList.of(
                Product.builder().itemType("foo").uuid(UUID.randomUUID()).monetaryAmount(monetaryAmount).build(),
                Product.builder().itemType("foo").uuid(UUID.randomUUID()).monetaryAmount(monetaryAmount.multiply(2)).build(),
                Product.builder().itemType("foo2").uuid(UUID.randomUUID()).monetaryAmount(monetaryAmount.multiply(4)).build()
        );

        final CouponApplicator couponApplicator = CouponApplicator
                .builder()
                .priceModifier((sum) -> sum.getMonetaryAmount().divide(2))
                .applicableProductFilter(
                        ProductAttributeFilter.builder().func(Product::getItemType).expectation("foo").build()
                )
                .build();
        final Map<UUID, MonetaryAmount> modifiedMapping = couponApplicator.modifyTotalCost(products);
        assertThat(modifiedMapping.entrySet(), hasSize(equalTo(2)));
        assertThat(modifiedMapping.get(products.get(0).getUuid()), equalTo(Money.of(.5, "USD")));
        assertThat(modifiedMapping.get(products.get(1).getUuid()), equalTo(Money.of(1, "USD")));
    }

}
