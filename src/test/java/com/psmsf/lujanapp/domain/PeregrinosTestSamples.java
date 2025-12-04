package com.psmsf.lujanapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PeregrinosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Peregrinos getPeregrinosSample1() {
        return new Peregrinos()
            .id(1L)
            .numeroEspecial(1)
            .apellido("apellido1")
            .nombre("nombre1")
            .numeroDocumento("numeroDocumento1")
            .telefono("telefono1")
            .pago(1)
            .aclaraciones("aclaraciones1");
    }

    public static Peregrinos getPeregrinosSample2() {
        return new Peregrinos()
            .id(2L)
            .numeroEspecial(2)
            .apellido("apellido2")
            .nombre("nombre2")
            .numeroDocumento("numeroDocumento2")
            .telefono("telefono2")
            .pago(2)
            .aclaraciones("aclaraciones2");
    }

    public static Peregrinos getPeregrinosRandomSampleGenerator() {
        return new Peregrinos()
            .id(longCount.incrementAndGet())
            .numeroEspecial(intCount.incrementAndGet())
            .apellido(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .numeroDocumento(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .pago(intCount.incrementAndGet())
            .aclaraciones(UUID.randomUUID().toString());
    }
}
