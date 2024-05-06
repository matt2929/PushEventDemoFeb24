package org.example.models.coupons;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class CouponApplicator {


  public ProductFilter productFilter;
  public PriceModifier priceModifier;

  public Map<UUID, Double> modifyTotalCost(List<Product> productList) {
    return productFilter.applyFilter(productList)
        .stream()
        .collect(Collectors.toMap(Product::getUuid,
            product -> priceModifier.modifyPrice(product)));
  }

  public interface PriceModifier {
    Double modifyPrice(Product product);
  }

}
