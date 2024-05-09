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

  public Map<String, Double> modifyTotalCost(List<ProductEntity> productEntityList) {
    return productFilter.applyFilter(productEntityList)
        .stream()
        .collect(Collectors.toMap(ProductEntity::getUuid,
            product -> priceModifier.modifyPrice(product)));
  }

  public interface PriceModifier {
    Double modifyPrice(ProductEntity productEntity);
  }

}
