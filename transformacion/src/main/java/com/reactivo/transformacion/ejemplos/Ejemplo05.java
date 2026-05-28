package com.reactivo.transformacion.ejemplos;

import reactor.core.publisher.Flux;

public class Ejemplo05 {
    public static void main(String[] args) {
        // ========== 1. CREAMOS DOS FLUJOS ==========
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");
        // ========== 2. ZIPWITH - COMBINACIÓN ELEMENTO A ELEMENTO ==========
        // Toma el PRIMER elemento de cada flujo y los combina
        // Luego el SEGUNDO elemento de cada flujo, etc
        flux1.zipWith(flux2, (first, second) -> first + second).subscribe(System.out::println);
    }
}
