package com.project.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class Ejemplo01 {

    static void main() {
        // Lista donde guardaremos los datos emitidos por Mono
        List<Integer> elementosFromMono = new ArrayList<>();
        // Lista donde guardaremos los datos emitidos por Flux
        List<Integer> elementosFromFlux = new ArrayList<>();

        // Crear un publisher Mono que emite como maximo un elemento
        Mono<Integer> mono = Mono.just(123);
        // Crear un publisher Flux que emite de 0 a multiples elementos
        Flux<Integer> flux = Flux.just(10, 11, 12, 13, 14);

        // Suscribirse al mono: cuando llegue el dato, lo agregamos a la lista.
        mono.subscribe(elementosFromMono::add);

        // Suscribirse al flux: por cada numero que llegue, lo agregamos a la lista.
        flux.subscribe(elementosFromFlux::add);

        // Imprimir las listas
        System.out.println(elementosFromMono);
        System.out.println(elementosFromFlux);
    }
}
