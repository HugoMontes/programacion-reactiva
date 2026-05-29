package com.manejo.excepciones.ejemplos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Ejemplo01 {
    // ========== 1. CREAMOS UN FLUJO CON ERROR ==========
    static void main() {
        Flux.just(2, 7, 10)                 // 3 elementos normales: 2, 7, 10
                .concatWith(Flux.error(new RuntimeException("Exception ocurred"))) // ERROR aquí
                .concatWith(Mono.just(12))   // Este elemento NUNCA se emitirá
                .onErrorReturn(72)
                .log()                            // Log para ver todos los eventos
                .subscribe();                     // Ejecuta el flujo
    }
}
