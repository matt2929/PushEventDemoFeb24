package org.example.models.coupons;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import lombok.Builder;
import org.example.models.Product;
import org.example.models.coupons.productfilters.ApplicableProductFilter;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public class CouponApplicator {


    public ApplicableProductFilter applicableProductFilter;
    public PriceModifier priceModifier;

    public Map<UUID, MonetaryAmount> modifyTotalCost(List<Product> shoppingCartContents){
        return applicableProductFilter.applicableProducts(shoppingCartContents)
                .stream()
                .collect(Collectors.toMap(Product::getUuid, product->priceModifier.modifyPrice(product)));
    }

    public interface PriceModifier {
        MonetaryAmount modifyPrice(Product product);
    }

}
