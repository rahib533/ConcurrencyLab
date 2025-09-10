package az.rahibjafarov;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(IntWithRaceCondition.class.getName());
        IntWithRaceCondition.run();

        System.out.println(IntWithSynchronized.class.getName());
        IntWithSynchronized.run();

        System.out.println(IntWithAtomicInteger.class.getName());
        IntWithAtomicInteger.run();
    }
}