package absl.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlanMutualTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PlanMutual getPlanMutualSample1() {
        return new PlanMutual().id(1L).categoria("categoria1").etiquetaReporte("etiquetaReporte1");
    }

    public static PlanMutual getPlanMutualSample2() {
        return new PlanMutual().id(2L).categoria("categoria2").etiquetaReporte("etiquetaReporte2");
    }

    public static PlanMutual getPlanMutualRandomSampleGenerator() {
        return new PlanMutual()
            .id(longCount.incrementAndGet())
            .categoria(UUID.randomUUID().toString())
            .etiquetaReporte(UUID.randomUUID().toString());
    }
}
