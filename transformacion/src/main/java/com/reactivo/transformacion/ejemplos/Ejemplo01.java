package com.reactivo.transformacion.ejemplos;

import reactor.core.publisher.Flux;

public class Ejemplo01 {
    static void main() {
        // ========== 1. FUENTE DE DATOS (Publisher) ==========
        // Creamos un Flux a partir de un array de 4 nombres
        Flux.fromArray(new String[]{"Tom", "Melissa", "Steve", "Megan"})
                // ========== 2. TRANSFORMACIÓN (Operador map) ==========
                // map() - Transforma CADA elemento del flujo
                // Entrada: String (nombre original)
                // Salida: String (nombre en mayúsculas)
                .map(String::toUpperCase)
                // ========== 3. CONSUMO (Subscriber) ==========
                // Imprime cada elemento transformado
                .subscribe(System.out::println);
    }
}
