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
  public List<Product> applyFilter(List<Product> products) {
    if (this.productFilter.isEmpty()) {
      return products.stream().sorted(
          Comparator.comparing(Product::getMonetaryAmount)
      ).limit(numProducts).collect(ImmutableList.toImmutableList());
    } else {
      return productFilter.get().applyFilter(products).stream().sorted(
          Comparator.comparing(Product::getMonetaryAmount)
      ).limit(numProducts).collect(ImmutableList.toImmutableList());
    }
  }

}