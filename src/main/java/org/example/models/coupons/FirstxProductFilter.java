package org.example.models.coupons;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FirstxProductFilter extends ProductFilter {
  final int numProducts;
  final Optional<ProductFilter> productFilter;

  public FirstxProductFilter(ProductFilter productFilter, int numProducts) {
    this.numProducts = numProducts;
    this.productFilter = Optional.of(productFilter);
  }

  @Override
  public List<ProductEntity> applyFilter(List<ProductEntity> productEntities) {
    if (this.productFilter.isEmpty()) {
      return productEntities.stream().sorted(
          Comparator.comparing(ProductEntity::getMonetaryAmount)
      ).limit(numProducts).collect(ImmutableList.toImmutableList());
    } else {
      return productFilter.get().applyFilter(productEntities).stream().sorted(
          Comparator.comparing(ProductEntity::getMonetaryAmount)
      ).limit(numProducts).collect(ImmutableList.toImmutableList());
    }
  }

}