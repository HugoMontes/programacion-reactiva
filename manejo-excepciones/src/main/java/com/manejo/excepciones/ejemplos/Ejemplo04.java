package com.manejo.excepciones.ejemplos;

import reactor.core.publisher.Flux;

public class Ejemplo04 {
    static void main(String[] args) {
        // ========== 1. FLUJO CON ERROR ==========
        Flux.just(2, 0, 10, 8, 12, 22, 24)    // 7 elementos
                .map(element -> {
                    if (element == 8) {
                        // Error original: RuntimeException genérico
                        throw new RuntimeException("Exception ocurred!");
                    }
                    return element;                 // Elementos normales pasan
                }).onErrorMap(ex -> {     // onErrorMap Transforma el ERROR
                    System.out.println("Exception : " + ex);    // Log del error original
                    // Transformamos RuntimeException → CustomException
                    return new CustomException(ex.getMessage(), ex);
                })
                .log()
                .subscribe();
    }
}
