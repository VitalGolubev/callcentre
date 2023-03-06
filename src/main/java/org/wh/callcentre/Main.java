package org.wh.callcentre;

import java.util.LinkedList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random rand = new Random();
        LinkedList<Operator> operators = new LinkedList<>();
        for (int i = 0; i < rand.nextInt(2, 5); i++) {
            operators.add(new Operator("OP#" + (i + 1)));
        }
        CallCentre<Operator> callCentre = new CallCentre<>(operators);

        int clientCount = rand.nextInt(3, 10);
        System.out.println("CallCentre need to receive call from " + clientCount + " clients\n");
        for (int i = 0; i < clientCount; i++) {
            new Client(callCentre, "CL#" + (i + 1)).start();
        }
    }

}
