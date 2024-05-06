package org.example.applications;

import org.example.models.rest.EmployeeController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@Import({EmployeeController.class})
public class DemoApplication implements Application {


  @Override
  public void run(String[] args) throws Exception {

    SpringApplication.run(DemoApplication.class, args);

  }

}
