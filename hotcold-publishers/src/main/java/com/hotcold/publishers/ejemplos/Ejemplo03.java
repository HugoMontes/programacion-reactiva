package com.hotcold.publishers.ejemplos;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class Ejemplo03 {
    static void main() throws InterruptedException {
        Flux<String> netFlux = Flux.fromStream(Ejemplo03::getVideo)
                .delayElements(Duration.ofSeconds(2))
                .publish()
                .refCount(2);

        System.out.println("=== Suscriptor 1 se suscribe en t=0s ===");
        netFlux.subscribe(part -> System.out.println("Suscriber 1 : " + part));

        Thread.sleep(5000);

        System.out.println("=== Suscriptor 2 se suscribe en t=5s ===");
        netFlux.subscribe(part -> System.out.println("Suscriber 2 : " + part));

        Thread.sleep(60000);

    }

    private static Stream<String> getVideo() {
        System.out.println("Request para el video");
        return Stream.of("part 1", "part 2", "part 3", "part 4", "part 5");
    }
}
