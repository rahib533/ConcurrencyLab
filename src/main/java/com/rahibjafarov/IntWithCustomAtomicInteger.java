package com.rahibjafarov;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class IntWithCustomAtomicInteger {
    static volatile int counter = 0;
    static final VarHandle VH;

    static {
        try {
            VH = MethodHandles.lookup()
                    .findStaticVarHandle(IntWithCustomAtomicInteger.class, "counter", int.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void increment() {
        int prev;
        do {
            prev = (int) VH.getVolatile();      // mövcud dəyəri oxuyur
        } while (!VH.compareAndSet(prev, prev + 1)); // CAS uğurlu olana qədər dönür
    }

    public static void run() throws InterruptedException {
        Runnable r = () -> {
            for (int i = 0; i < 1_000_000; i++) increment();
        };

        Thread t1 = new Thread(r), t2 = new Thread(r);
        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Counter: " + counter);
    }
}
