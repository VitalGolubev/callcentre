package org.wh.callcentre;

import java.util.Random;

public class Operator {

    private final String name;
    private static final Random random = new Random();

    public Operator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void using() throws InterruptedException {
        int usingTime = random.nextInt(1000);
        System.out.println("Operator " + name + " is using for " + usingTime + " millis");
        Thread.sleep(usingTime);
    }

}
