package org.example.models.coupons;

import com.google.common.collect.ImmutableList;


import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AttributeBasedProductFilter<T> extends ProductFilter {

    Function<Product, T> attribute;
    T expectation;
    Optional<ProductFilter> productFilter;


    public AttributeBasedProductFilter(Function<Product, T> attribute, T expectation) {
        this.attribute = attribute;
        this.expectation = expectation;
        this.productFilter = Optional.empty();
    }

    @Override
    public List<Product> applyFilter(List<Product> products) {
        if (this.productFilter.isEmpty()) {
            return products.stream().filter(
                    product -> attribute.apply(product).equals(expectation)
            ).collect(ImmutableList.toImmutableList());
        } else {
            return productFilter.get().applyFilter(products).stream().filter(
                    product -> attribute.apply(product).equals(expectation)
            ).collect(ImmutableList.toImmutableList());
        }

    }
}
