package az.rahibjafarov;

public class IntWithSynchronized {
    static int counter = 0;

    public static void run() throws InterruptedException {
        Runnable r = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                increment();
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Counter: " + counter);
    }

    private static synchronized void increment() {
        counter++;
    }
}
