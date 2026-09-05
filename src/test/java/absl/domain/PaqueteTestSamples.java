package absl.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaqueteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Paquete getPaqueteSample1() {
        return new Paquete().id(1L).paqDescrip("paqDescrip1").nombre("nombre1").descripcion("descripcion1");
    }

    public static Paquete getPaqueteSample2() {
        return new Paquete().id(2L).paqDescrip("paqDescrip2").nombre("nombre2").descripcion("descripcion2");
    }

    public static Paquete getPaqueteRandomSampleGenerator() {
        return new Paquete()
            .id(longCount.incrementAndGet())
            .paqDescrip(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
