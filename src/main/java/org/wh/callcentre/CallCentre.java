package org.wh.callcentre;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CallCentre<T> {

    private final Semaphore semaphore;
    private final Queue<T> operators = new LinkedList<>();
    private static final Random random = new Random();

    public CallCentre(Queue<T> operators) {
        this.semaphore = new Semaphore(operators.size(), true);
        this.operators.addAll(operators);
        System.out.println("CallCentre have " + operators.size() + " operators");
    }

    public T getOperator() throws OperatorException, InterruptedException {
        if (semaphore.tryAcquire(random.nextInt(100), TimeUnit.MILLISECONDS)) {
            return operators.poll();
        }

        throw new OperatorException("All operator busy right now");
    }

    public void returnOperator(T operator) {
        operators.add(operator);
        semaphore.release();
    }

}
