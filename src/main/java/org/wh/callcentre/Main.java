package org.wh.callcentre;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CallCentre callCentre = new CallCentre(3);

        for (int i = 1; i <= 30; i++) {
            Client client = new Client(i, callCentre);
            Thread clientThread = new Thread(client);
            clientThread.start();
        }

        Thread.sleep(10000);
        callCentre.shutdown();
    }

}
