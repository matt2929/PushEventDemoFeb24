package org.example.models.coupons;

import java.util.List;


public abstract class ProductFilter {

  public abstract List<ProductEntity> applyFilter(List<ProductEntity> productEntities);

}
