package org.example.models.coupons.productfilters;

import org.example.models.Product;

import java.util.List;

public interface ApplicableProductFilter {

    public List<Product> applicableProducts(final List<Product> products);

}
