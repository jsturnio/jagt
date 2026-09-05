package absl.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PrestacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Prestacion getPrestacionSample1() {
        return new Prestacion().id(1L).codigo(1).codigoInos("codigoInos1");
    }

    public static Prestacion getPrestacionSample2() {
        return new Prestacion().id(2L).codigo(2).codigoInos("codigoInos2");
    }

    public static Prestacion getPrestacionRandomSampleGenerator() {
        return new Prestacion().id(longCount.incrementAndGet()).codigo(intCount.incrementAndGet()).codigoInos(UUID.randomUUID().toString());
    }
}
