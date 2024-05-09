package org.example;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.applications.Application;
import org.example.applications.Dealer;
import org.example.applications.DemoApplication;
import org.example.applications.Player;
import org.example.applications.Puller;
import org.example.applications.Pusher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Main {


  public static void main(String[] args) throws Exception {
    final Map<String, Application> applicationKeyword = ImmutableMap.of(
        "PUSH", new Pusher(),
        "PULL", new Puller(),
        "PLAYER", new Player(),
        "DEALER", new Dealer(),
        "SPRING", new DemoApplication()
    );
    final String availableKeysStr = applicationKeyword.keySet().stream()
        .collect(Collectors.joining(", ", "[", "]"));

    System.out.println(Arrays.stream(args)
        .collect(Collectors.joining(", ", "[", "]")));

    if (args.length == 0) {
      throw new RuntimeException(
          String.format("no args provided one of %s required",
              availableKeysStr));
    }

    final String arg = args[args.length - 1].toUpperCase();

    if (!applicationKeyword.containsKey(arg)) {
      throw new RuntimeException(
          String.format("provided arg '%s' is not one of %s", arg,
              availableKeysStr));
    }

    applicationKeyword.get(arg).run(args);
  }
}