package com.reactivo.transformacion.ejemplos;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Ejemplo03 {
    static void main(String[] args) {
        // ========== 1. FLUJO ORIGINAL ==========
        // Flux con 4 nombres
        Flux.fromArray(new String[]{"Tom", "Melissa", "Steve", "Megan"})
                // ========== 2. flatMap() - TRANSFORMACIÓN ASÍNCRONA ==========
                // flatMap() - Aplica una función que retorna UN Publisher (Mono/Flux)
                // Por CADA elemento del flujo original, se crea un NUEVO flujo reactivo
                // Luego, "aplana" todos esos flujos en UN SOLO FLUJO de salida
                .flatMap(Ejemplo03::ponerNombreModificadoEnMono)
                // flatMap recibe: nombre "Tom" → Mono.just("Tom modificado")
                //               nombre "Melissa" → Mono.just("Melissa modificado")
                //               etc
                .subscribe(System.out::println);
    }

    // ========== FUNCIÓN QUE RETORNA UN Mono ==========
    // Toma un String y devuelve un Mono<String> (operación asíncrona)
    private static Mono<String> ponerNombreModificadoEnMono(String nombre) {
        // Concatena " modificado" al nombre y lo envuelve en un Mono
        return Mono.just(nombre.concat(" modificado"));
        // En un caso real, aquí podría haber:
        // - Llamada a base de datos reactiva (R2DBC)
        // - Llamada a API externa con WebClient
        // - Operación que toma tiempo
    }
}
