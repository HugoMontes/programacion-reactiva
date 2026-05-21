package com.project.reactor;

import reactor.core.publisher.Mono;

public class Ejemplo03 {

    static void main() {
        Mono<String> mono = Mono.error(new RuntimeException("Excepcion ocurrida"));

        mono.subscribe(
                data -> System.out.println("Dato: " + data),    // onNext
                err -> System.out.println("Error: " + err), // onError
                () -> System.out.println("Completado!")               // onComplete
        );
    }
}
