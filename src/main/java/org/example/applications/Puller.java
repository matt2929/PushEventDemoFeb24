package org.example.applications;

public class Puller implements Application {
    @Override
    public void run() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.printf("yo %d%n", i);
        }
    }
}
