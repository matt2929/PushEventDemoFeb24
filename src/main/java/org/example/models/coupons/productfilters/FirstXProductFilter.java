package org.example.models.coupons.productfilters;

import com.google.common.collect.ImmutableList;
import org.example.models.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FirstXProductFilter extends ProductFilter {
    int x;
    Optional<ProductFilter> productFilter;

    public FirstXProductFilter(ProductFilter productFilter, int x) {
        this.x = x;
        this.productFilter = Optional.of(productFilter);
    }


    @Override
    public List<Product> applyFilter(List<Product> products) {
        if(this.productFilter.isEmpty()){
            return products.stream().sorted(
                    Comparator.comparing(Product::getMonetaryAmount)
            ).limit(x).collect(ImmutableList.toImmutableList());
        }else{
            return productFilter.get().applyFilter(products).stream().sorted(
                    Comparator.comparing(Product::getMonetaryAmount)
            ).limit(x).collect(ImmutableList.toImmutableList());
        }
    }

}