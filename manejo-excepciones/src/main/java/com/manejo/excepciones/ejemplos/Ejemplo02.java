package com.manejo.excepciones.ejemplos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Ejemplo02 {
    static void main(String[] args) {
        // ========== 1. FLUJO CON ERROR ==========
        Flux.just(2, 7, 10)                         // 3 elementos normales
                .concatWith(Flux.error(new RuntimeException("Exception ocurred"))) // ERROR
                .concatWith(Mono.just(12))           // Sin onErrorResume, esto no llegaría
                .onErrorResume(err -> {         // Manejo del error
                    System.out.println("Error : " + err); // Log del error
                    return Mono.just(12);            // Valor alternativo
                })
                .log()                                    // Log para ver todos los eventos
                .subscribe();                             // Ejecuta el flujo

    }
}
