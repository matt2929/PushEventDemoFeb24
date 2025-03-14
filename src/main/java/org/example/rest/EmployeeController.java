package org.example.rest;

import java.util.List;
import java.util.UUID;
import org.example.models.coupons.ProductEntity;
import org.example.mongodb.ProductCrudderMongo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

  @GetMapping("/product/{id}")
  public ProductEntity getProductById(@PathVariable UUID id) {
    try (ProductCrudderMongo productCrudderMongo = new ProductCrudderMongo()) {
      return productCrudderMongo.getProductById(id).orElseGet(() -> null);
    }
  }

  @GetMapping("/listproduct")
  public List<ProductEntity> listProducts() {
    try (ProductCrudderMongo productCrudderMongo = new ProductCrudderMongo()) {
      return productCrudderMongo.getAllProducts();
    }
  }

  @PostMapping("/createProduct")
  String createProduct(@RequestBody ProductEntity productEntity) {
    try (ProductCrudderMongo productCrudderMongo = new ProductCrudderMongo()) {
      productCrudderMongo.insertEntity(productEntity);
    }
    return String.format("Successfully wrote %s", productEntity.uuid);
  }

}
