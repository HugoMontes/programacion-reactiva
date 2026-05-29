package com.manejo.excepciones.ejemplos;

import reactor.core.publisher.Flux;

public class Ejemplo03 {
    public static void main(String[] args) {
        // ========== 1. FLUJO CON ELEMENTOS ==========
        Flux.just(2,0,10,8,12,22,24)   // 7 elementos, incluyendo un 0
                .map(element -> {     // Transformación que PUEDE fallar
                    if(element == 0){
                        // Cuando el elemento es 0, lanzamos excepción
                        throw new RuntimeException("Exception ocurred!");
                    }
                    return element;           // Elementos normales pasan
                }).onErrorContinue((ex,element) -> {    // Manejo por elemento
                    System.out.println("Exception : " + ex);
                    System.out.println("El elemento que causa la excepcion es : " + element);
                    // IMPORTANTE: No emitimos nada como reemplazo
                    // Simplemente logueamos y continuamos con el siguiente elemento
                })
                .log()
                .subscribe();
    }
}
