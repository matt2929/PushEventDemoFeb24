package org.example.models.coupons.productfilters;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import org.example.models.Product;

import java.util.List;
import java.util.function.Function;

@Builder
public class ProductAttributeFilter<T> implements ApplicableProductFilter {

    T expectation;
    Function<Product, T> func;


    @Override
    public List<Product> applicableProducts(List<Product> products) {
        return products.stream()
                .filter(product -> func.apply(product).equals(expectation))
                .collect(ImmutableList.toImmutableList());
    }

}
