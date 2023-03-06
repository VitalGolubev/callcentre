package org.wh.callcentre;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class CallCentre<T> {

    private final Semaphore semaphore;
    private final Queue<T> operators = new LinkedList<>();

    public CallCentre(Queue<T> operators) {
        this.semaphore = new Semaphore(operators.size(), true);
        this.operators.addAll(operators);
        System.out.println("CallCentre have " + operators.size() + " operators");
    }

    public T getOperator() throws OperatorException {
        if (semaphore.tryAcquire()) {
            return operators.poll();
        }

        throw new OperatorException("All operator busy right now");
    }

    public void returnOperator(T operator) {
        operators.add(operator);
        semaphore.release();
    }

}
