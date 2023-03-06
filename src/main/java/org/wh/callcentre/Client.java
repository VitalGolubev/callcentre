package org.wh.callcentre;

import java.util.Random;

public class Client extends Thread {

    private final String clientName;
    private final CallCentre<Operator> callCentre;
    private final Random random = new Random();

    public Client(CallCentre<Operator> callCentre, String name) {
        this.callCentre = callCentre;
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public void run() {
        Operator operator = null;
        boolean dialing = true;
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            System.out.println("Client " + this.getClientName() + " externally interrupted " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        System.out.println("Client " + this.getClientName() + " start calling");
        while (dialing) {
            try {
                operator = callCentre.getOperator();
                System.out.println("Client " + this.getClientName() + " speaking with operator: " + operator.getName());
                operator.using();
            } catch (OperatorException ignored) {
            } catch (InterruptedException e) {
                System.out.println("Client " + this.getClientName() + " externally interrupted " + e.getMessage());
                Thread.currentThread().interrupt();
            } finally {
                if (operator != null) {
                    dialing = false;
                    System.out.println(
                        "Client " + this.getClientName() + " hung up and release Operator: " + operator.getName());
                    callCentre.returnOperator(operator);
                } else {
                    dialing = isDialing();
                }
            }
        }
    }

    private boolean isDialing() {
        boolean dialing = random.nextBoolean();
        if (dialing) {
            System.out.println("Client " + getClientName() + " can't dial and decide to redial");
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                System.out.println("Client " + this.getClientName() + " externally interrupted " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("Client " + getClientName() + " can't dial and decide to NOT redial");
        }
        return dialing;
    }

}
