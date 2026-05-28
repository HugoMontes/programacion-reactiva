package com.reactivo.contrapresion.ejemplos;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class Ejemplo02 {
    static void main() {
        // ========== 1. CREAMOS EL PUBLISHER ==========
        // Flux.range(1, 100) emite números del 1 al 100
        // .log() nos permite ver TODOS los eventos (suscripción, peticiones, datos)
        Flux<Integer> flux = Flux.range(1, 100).log();

        // ========== 2. SUBSCRIBER PERSONALIZADO CON CONTRAPRESIÓN ==========
        flux.subscribe(new BaseSubscriber<Integer>() {

            // METODO CLAVE: hookOnSubscribe()
            // Se ejecuta CUANDO el subscriber recibe la Subscription
            // Aquí es donde DECIMOS cuántos datos queremos
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                // ¡CONTRAPRESIÓN EN ACCIÓN!
                // NO pedimos todos los 100 elementos
                // Solo pedimos 5 elementos inicialmente
                request(10);
                // Si hicieramos request(Long.MAX_VALUE) sería igual a subscribe() normal
            }

            @Override
            protected void hookOnNext(Integer value) {
                if(value == 5){
                    cancel();
                }
            }
        });
    }
}
