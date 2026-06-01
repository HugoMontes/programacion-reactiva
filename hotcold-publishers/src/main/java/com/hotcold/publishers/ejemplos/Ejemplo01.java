package com.hotcold.publishers.ejemplos;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class Ejemplo01 {

    static void main(String[] args) throws InterruptedException {
        // ========== 1. CREACIÓN DEL COLD PUBLISHER ==========
        // CADA suscripción ejecutará getVideo() de NUEVO
        Flux<String> netFlux = Flux.fromStream(Ejemplo01::getVideo)
                .delayElements(Duration.ofSeconds(2)); // Cada "parte" tarda 2 segundos

        // ========== 2. PRIMER SUSCRIPTOR (t=0s) ==========
        System.out.println("=== Suscriptor 1 se suscribe en t=0s ===");
        netFlux.subscribe(part -> System.out.println("Suscriber 1 : " + part));
        // Este subscriber activa la PRIMERA ejecución de getVideo()
        // Verá: part 1, part 2, part 3, part 4, part 5 (cada 2 segundos)

        // ========== 3. ESPERA DE 5 SEGUNDOS ==========
        Thread.sleep(5000);

        // ¿Qué ha pasado durante estos 5 segundos?
        // t=0s:  Suscriptor 1 se suscribe → getVideo() se ejecuta → "Request para el video"
        // t=2s:  Suscriptor 1 recibe "part 1"
        // t=4s:  Suscriptor 1 recibe "part 2"
        // t=5s:  ¡Ahora se suscribe el Suscriptor 2

        // ========== 4. SEGUNDO SUSCRIPTOR (t=5s) ==========
        System.out.println("=== Suscriptor 2 se suscribe en t=5s ===");
        netFlux.subscribe(part -> System.out.println("Suscriber 2 : " + part));
        // IMPORTANTE: Este subscriber ejecuta getVideo() de NUEVO
        // Verá TODAS las partes desde el principio: part 1, part 2, part 3, part 4, part 5
        // ¡No importa que ya se hayan emitido antes!

        // ========== 5. MANTENER EL PROGRAMA VIVO ==========
        Thread.sleep(60000);  // Espera 60 segundos para ver toda la ejecución
    }

    // ========== FUENTE DE DATOS (COLD) ==========
    private static Stream<String> getVideo() {
        // Se ejecuta CADA vez que alguien se suscribe
        System.out.println("Request para el video");
        return Stream.of("part 1", "part 2", "part 3", "part 4", "part 5");
    }
}
