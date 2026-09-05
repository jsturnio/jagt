package absl.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PracticaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Practica getPracticaSample1() {
        return new Practica().id(1L).cantidad(1);
    }

    public static Practica getPracticaSample2() {
        return new Practica().id(2L).cantidad(2);
    }

    public static Practica getPracticaRandomSampleGenerator() {
        return new Practica().id(longCount.incrementAndGet()).cantidad(intCount.incrementAndGet());
    }
}
