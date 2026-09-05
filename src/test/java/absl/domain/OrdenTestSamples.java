package absl.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrdenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Orden getOrdenSample1() {
        return new Orden().id(1L).afiliado(1L).protocolo(1L);
    }

    public static Orden getOrdenSample2() {
        return new Orden().id(2L).afiliado(2L).protocolo(2L);
    }

    public static Orden getOrdenRandomSampleGenerator() {
        return new Orden().id(longCount.incrementAndGet()).afiliado(longCount.incrementAndGet()).protocolo(longCount.incrementAndGet());
    }
}
