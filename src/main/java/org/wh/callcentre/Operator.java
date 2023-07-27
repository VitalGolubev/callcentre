package org.wh.callcentre;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Operator implements Runnable {

    private final int id;
    private final BlockingQueue<Client> clients;
    private final CallCentre callCentre;
    private final List<String> servedClients = new ArrayList<>();

    public Operator(int id, BlockingQueue<Client> clients, CallCentre callCentre) {
        this.id = id;
        this.clients = clients;
        this.callCentre = callCentre;
    }

    @Override
    public void run() {
        System.out.printf("OPERATOR %d CREATED and ready to serve clients%n", id);
        while (!callCentre.isShutdown()) {
            try {
                Client client = clients.poll(1, TimeUnit.SECONDS);
                if (client != null) {
                    System.out.printf("OPERATOR %d is serving client %d%n", id, client.getId());
                    Thread.sleep(client.getServiceTime());
                    System.out.printf("OPERATOR %d finished serving client %d%n", id, client.getId());
                    client.setServed(true);
                    servedClients.add(String.valueOf(client.getId()));
                } else {
                    System.out.printf("OPERATOR %d is idle%n", id);
                }
            } catch (InterruptedException e) {
                System.out.printf("OPERATOR %d INTERRUPTED%n", id);
                Thread.currentThread().interrupt();
            }
        }
        System.out.printf("OPERATOR %d STOPPED and served next clients: %s%n", id, String.join(", ", servedClients));
    }

}
