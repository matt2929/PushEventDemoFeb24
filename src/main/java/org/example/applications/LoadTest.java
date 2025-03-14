package org.example.applications;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.example.models.coupons.ProductEntity;

public class LoadTest implements Application {
  @Override
  public void run(String[] args) throws Exception {
    for (int i = 0; i < 10000; i++) {
      sendMessageWithRetries();
    }
  }

  private Void sendMessage() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    final String jsonPayload = generateEntityJson();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://app-play-1:8080/createProduct"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
        .build();
    final HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.printf("The response was %s%n", response.body());
    return null;
  }

  private String generateEntityJson() {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ProductEntity productEntity = ProductEntity.builder()
        .itemType(Math.random() + "")
        .monetaryAmount(Math.random())
        .uuid(UUID.randomUUID().toString())
        .build();
    try {
      return ow.writeValueAsString(productEntity);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }


  public void sendMessageWithRetries() {
    Callable<Void> retrieveMessage = this::sendMessage;
    Status<Void> connectionResult =
        new CallExecutorBuilder().config(getDefaultConfig()).build().execute(retrieveMessage);
  }


  private RetryConfig getDefaultConfig() {
    return new RetryConfigBuilder().withMaxNumberOfTries(5)
        .withFixedBackoff().withDelayBetweenTries(5, SECONDS)
        .retryOnSpecificExceptions(
            ConnectException.class
        )
        .build();
  }

}
