package org.example.models.coupons;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AttributeBasedProductFilter<T> extends ProductFilter {

  Function<ProductEntity, T> attribute;
  T expectation;
  Optional<ProductFilter> productFilter;


  public AttributeBasedProductFilter(Function<ProductEntity, T> attribute, T expectation) {
    this.attribute = attribute;
    this.expectation = expectation;
    this.productFilter = Optional.empty();
  }

  @Override
  public List<ProductEntity> applyFilter(List<ProductEntity> productEntities) {
    if (this.productFilter.isEmpty()) {
      return productEntities.stream().filter(
          product -> attribute.apply(product).equals(expectation)
      ).collect(ImmutableList.toImmutableList());
    } else {
      return productFilter.get().applyFilter(productEntities).stream().filter(
          product -> attribute.apply(product).equals(expectation)
      ).collect(ImmutableList.toImmutableList());
    }

  }
}
