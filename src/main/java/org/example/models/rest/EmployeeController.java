package org.example.models.rest;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.example.models.coupons.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();


  @GetMapping("/employees/{id}")
  public Product greeting(@PathVariable UUID id) {
    return Product.builder()
        .uuid(id)
        .itemType("foo")
        .monetaryAmount(1d)
        .build();
  }

  @PostMapping("/createEmployee")
  Product newEmployee(@RequestBody Product product) {
    return Product.builder()
        .uuid(UUID.randomUUID())
        .itemType("I got it!")
        .monetaryAmount(123d)
        .build();
  }

}
