package org.example.models.coupons;

import lombok.Builder;
import org.example.models.Product;
import org.example.models.coupons.productfilters.ProductFilter;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public class CouponApplicator {


    public ProductFilter productFilter;
    public PriceModifier priceModifier;

    public Map<UUID, MonetaryAmount> modifyTotalCost(List<Product> productList){
        return productFilter.applyFilter(productList)
                .stream()
                .collect(Collectors.toMap(Product::getUuid, product->priceModifier.modifyPrice(product)));
    }

    public interface PriceModifier {
        MonetaryAmount modifyPrice(Product product);
    }

}
