package org.wh.callcentre;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {

    private final int id;
    private final CallCentre callCentre;
    private final Random random = new Random();
    private volatile boolean served;
    private boolean phoned = true;
    private static final Integer MAX_WAIT_TIME_IN_MILLIS = 3000;

    public Client(int id, CallCentre callCentre) {
        this.id = id;
        this.callCentre = callCentre;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        sleepBeforeCall();
        makeCall();
        sleepAfterCall();
        System.out.printf("CLIENT %d thread has STOPPED %sserved%n", id, phoned ? "" : "NOT ");
        if (!phoned) {
            callCentre.addNoServedClient(this);
        }
    }

    private void sleepAfterCall() {
        while (!served) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void makeCall() {
        System.out.printf("CLIENT %d calls CallCentre%n", id);
        boolean tryToRecall = true;
        while (tryToRecall) {
            if (callCentre.assignOperator(this, getRandomTime(), TimeUnit.MILLISECONDS)) {
                System.out.printf("CLIENT %d assigned to operator%n", id);
                tryToRecall = false;
            } else {
                tryToRecall = random.nextBoolean();
                if (tryToRecall) {
                    System.out.printf("CLIENT %d WILL call again %n", id);
                    try {
                        Thread.sleep(getRandomTime());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    served = true;
                    phoned = false;
                    System.out.printf("CLIENT %d WILL NOT call again%n", id);
                }
            }
        }
    }

    private void sleepBeforeCall() {
        int millis = getRandomTime();
        System.out.printf("CLIENT %d is created and ready calls CallCentre through %d millis%n", id, millis);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getServiceTime() {
        return getRandomTime();
    }

    private int getRandomTime() {
        return random.nextInt(MAX_WAIT_TIME_IN_MILLIS);
    }

    public void setServed(boolean served) {
        this.served = served;
    }

}
