package absl.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NomencladorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Nomenclador getNomencladorSample1() {
        return new Nomenclador().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Nomenclador getNomencladorSample2() {
        return new Nomenclador().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Nomenclador getNomencladorRandomSampleGenerator() {
        return new Nomenclador()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
