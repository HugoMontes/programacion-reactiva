package com.reactivo.transformacion.ejemplos;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class EjemploTest02 {
    @Test
    public void testMergeWith() {
        // Verificamos el flujo resultante del merge
        StepVerifier.create(retornarMerge())
                .expectNext("a")   // ← Primero llega 'a' (100ms)
                .expectNext("c")   // ← Luego 'c' (125ms) - ¡pero 'b' aún no llegó!
                .expectNext("b")   // ← Luego 'b' (200ms)
                .expectNext("d")   // ← Finalmente 'd' (250ms)
                .expectComplete()
                .verify();
    }

    private static Flux<String> retornarMerge() {
        // FLUX 1: Emite "a", "b" con delay de 100ms entre elementos
        Flux<String> firstFlux = Flux.fromArray(new String[]{"a", "b"})
                .delayElements(Duration.ofMillis(100));
        // FLUX 2: Emite "c", "d" con delay de 125ms entre elementos
        Flux<String> secondFlux = Flux.fromArray(new String[]{"c", "d"})
                .delayElements(Duration.ofMillis(125));
        // ========== MERGEWITH - COMBINACIÓN CONCURRENTE =========
        // Los flujos se EJECUTAN EN PARALELO y los elementos se entrelazan según llegan
        return firstFlux.mergeWith(secondFlux);
    }
}
