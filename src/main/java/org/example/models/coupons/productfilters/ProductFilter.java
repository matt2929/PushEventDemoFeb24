package org.example.models.coupons.productfilters;

import org.example.models.Product;

import java.util.List;


public abstract class ProductFilter {

    public abstract List<Product> applyFilter(List<Product> products);

}
