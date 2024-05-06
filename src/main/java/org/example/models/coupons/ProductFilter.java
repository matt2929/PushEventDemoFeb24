package org.example.models.coupons;

import java.util.List;


public abstract class ProductFilter {

  public abstract List<Product> applyFilter(List<Product> products);

}
