package com.project.reactor;

import reactor.core.publisher.Flux;

public class Ejemplo04 {

    static void main() {
        // Crear un Flux a partir de un array de Strings
        // Flux.fromArray() convierte cualquier array en un flujo reactivo
        // Cada elemento del array se emitirá como un evento onNext separado
        Flux<String> flux = Flux.fromArray(new String[]{"data1", "data2", "data3"});

        // Suscribirse al Flux
        // System.out::println es una referencia de metodo (equivalente a x -> System.out.println(x))
        // Esta lambda maneja CADA evento onNext que emite el Flux
        // El Flux emitirá 3 eventos onNext (uno por cada elemento)
        flux.subscribe(System.out::println);

        // Esto es síncrono porque los datos están en memoria
        // Si los datos vinieran de una BD o API, sería asíncrono
    }
}
