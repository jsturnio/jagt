package absl.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BioquimicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Bioquimico getBioquimicoSample1() {
        return new Bioquimico()
            .id(1L)
            .matricula(1L)
            .cuit("cuit1")
            .domicilioProfesional("domicilioProfesional1")
            .email("email1")
            .telefono("telefono1")
            .nombreEnDosep("nombreEnDosep1")
            .nroPrestadorOsde(1)
            .ingBrutos("ingBrutos1")
            .nroJubilacion("nroJubilacion1")
            .nroLaboratorio("nroLaboratorio1")
            .nombreCompleto("nombreCompleto1");
    }

    public static Bioquimico getBioquimicoSample2() {
        return new Bioquimico()
            .id(2L)
            .matricula(2L)
            .cuit("cuit2")
            .domicilioProfesional("domicilioProfesional2")
            .email("email2")
            .telefono("telefono2")
            .nombreEnDosep("nombreEnDosep2")
            .nroPrestadorOsde(2)
            .ingBrutos("ingBrutos2")
            .nroJubilacion("nroJubilacion2")
            .nroLaboratorio("nroLaboratorio2")
            .nombreCompleto("nombreCompleto2");
    }

    public static Bioquimico getBioquimicoRandomSampleGenerator() {
        return new Bioquimico()
            .id(longCount.incrementAndGet())
            .matricula(longCount.incrementAndGet())
            .cuit(UUID.randomUUID().toString())
            .domicilioProfesional(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .nombreEnDosep(UUID.randomUUID().toString())
            .nroPrestadorOsde(intCount.incrementAndGet())
            .ingBrutos(UUID.randomUUID().toString())
            .nroJubilacion(UUID.randomUUID().toString())
            .nroLaboratorio(UUID.randomUUID().toString())
            .nombreCompleto(UUID.randomUUID().toString());
    }
}
