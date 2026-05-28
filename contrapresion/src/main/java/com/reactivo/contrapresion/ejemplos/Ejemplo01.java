package com.reactivo.contrapresion.ejemplos;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;

public class Ejemplo01 {
    static void main() {
        // ========== 1. CREAMOS EL FLUJO ==========
        // Fuente de datos: lista de 5 ciudades
        Flux<String> ciudades = Flux.fromIterable(
                new ArrayList<>(Arrays.asList("New York", "London", "Paris", "Toronto", "Rome"))
        );
        // NOTA: Usamos ArrayList + Arrays.asList() para crear una lista mutable
        // Pero .fromIterable() funciona con cualquier Iterable (List, Set, etc.)

        // ========== 2. LOGGING ==========
        // .log() es un OPERADOR que registra TODOS los eventos del flujo
        // Muestra en consola: suscripción, peticiones, emisiones, completado
        ciudades.log().subscribe(); // Ejecuta el flujo
    }
}
