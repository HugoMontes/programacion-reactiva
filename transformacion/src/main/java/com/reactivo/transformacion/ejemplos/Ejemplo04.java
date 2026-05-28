package com.reactivo.transformacion.ejemplos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Ejemplo04 {
    public static void main(String[] args) {

        // ========== 1. CREAMOS LAS FUENTES ==========
        // Flux con 3 elementos
        Flux<String> flux = Flux.fromArray(new String[]{"a", "b", "c"});

        // Mono con 1 elemento (¡diferente tipo de Publisher!)
        Mono<String> mono = Mono.just("f");

        // ========== 2. CONCATWITH ==========
        // concatWith() es el metodo de instancia equivalente a Flux.concat()
        // Concatena: primero TODOS los elementos del flux, luego el mono
        Flux<String> fluxConcat = flux.concatWith(mono);
        // Equivalente a: Flux.concat(flux, mono)

        // ========== 3. CONSUMO ==========
        fluxConcat.subscribe(element -> System.out.println(element + " "));
    }
}
