package absl.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MutualTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Mutual getMutualSample1() {
        return new Mutual()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .cuit("cuit1")
            .domicilio("domicilio1")
            .telefono("telefono1")
            .email("email1");
    }

    public static Mutual getMutualSample2() {
        return new Mutual()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .cuit("cuit2")
            .domicilio("domicilio2")
            .telefono("telefono2")
            .email("email2");
    }

    public static Mutual getMutualRandomSampleGenerator() {
        return new Mutual()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .cuit(UUID.randomUUID().toString())
            .domicilio(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
