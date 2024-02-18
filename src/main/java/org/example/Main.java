package org.example;

import org.example.applications.Application;
import org.example.applications.Puller;
import org.example.applications.Pusher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws Exception {
        final Map<String, Application> applicationKeyword = new HashMap<>();
        applicationKeyword.put("PUSH", new Pusher());
        applicationKeyword.put("PULL", new Puller());
        final String availableKeysStr = applicationKeyword.keySet().stream().collect(Collectors.joining(", ", "[", "]"));

        System.out.println(Arrays.stream(args).collect(Collectors.joining(", ", "[", "]")));

        if (args.length == 0) {
            throw new RuntimeException(String.format("no args provided one of %s required", availableKeysStr));
        }

        final String arg = args[args.length - 1].toUpperCase();

        if (!applicationKeyword.containsKey(arg)) {
            throw new RuntimeException(String.format("provided arg '%s' is not one of %s", arg, availableKeysStr));
        }

        applicationKeyword.get(arg).run();
    }
}