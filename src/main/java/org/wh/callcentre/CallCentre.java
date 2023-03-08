package org.wh.callcentre;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CallCentre {

    private final BlockingQueue<Client> clients;
    private final ExecutorService executor;
    private boolean shutdown = false;
    private final List<Client> noServedClients = new ArrayList<>();

    public CallCentre(int numOperators) {
        System.out.printf("CALL-CENTRE CREATED with %d operators%n", numOperators);
        clients = new LinkedBlockingQueue<>(numOperators);
        executor = Executors.newFixedThreadPool(numOperators);
        for (int i = 1; i <= numOperators; i++) {
            Operator operator = new Operator(i, clients, this);
            executor.submit(operator);
        }
    }

    public boolean assignOperator(Client client, long timeToWaitInQueue, TimeUnit timeUnit) {
        boolean success = false;
        if (executor.isShutdown()) {
            System.out.println("CALL-CENTRE CLOSED and can't receive call from clients");
            return false;
        }

        try {
            success = clients.offer(client, timeToWaitInQueue, timeUnit);
        } catch (InterruptedException e) {
            System.out.println("CALL-CENTRE interrupted");
            Thread.currentThread().interrupt();
        }

        if (success) {
            System.out.printf("CALL-CENTRE assigned client %d to an operator%n", client.getId());
        } else {
            System.out.printf("CALL-CENTRE could not assign client %d to an operator. All operators busy%n",
                client.getId());
        }

        return success;
    }

    public void shutdown() {
        System.out.printf("%n======================%nCALL-CENTRE wait serving clients before STOP" +
            ".%n======================%n%n");
        executor.shutdown();
        while (!clients.isEmpty() && !executor.isTerminated()) {
            try {
                System.out.printf("CALL-CENTER_SHUTDOWN %b, %d%n", clients.isEmpty(),
                    ((ThreadPoolExecutor) executor).getActiveCount());
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        shutdown = true;
        System.out.printf("%n======================%nCALL-CENTRE STOPPED and not served next clients: %s%n"
                + "======================%n%n",
            noServedClients.stream().map(s -> String.valueOf(s.getId())).collect(Collectors.joining(", ")));
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void addNoServedClient(Client client) {
        noServedClients.add(client);
    }

}
