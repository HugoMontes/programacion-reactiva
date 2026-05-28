package com.reactivo.transformacion.ejemplos;

import reactor.core.publisher.Flux;

public class Ejemplo02 {
    static void main() {
        Flux.fromArray(new String[]{"Tom", "Melissa", "Steve", "Megan"})
                // ========== FILTRO (Operador filter) ==========
                // filter() - SOLO deja pasar los elementos que cumplen la condición
                .filter(nombre -> nombre.length() > 5)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
    }
}
