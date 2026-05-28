package com.reactivo.transformacion.ejemplos;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class EjemploTest01 {
    @Test
    public void testTransformMap() {
        // Lista con 5 nombres (fuente de datos tradicional)
        List<String> listaNombres = Arrays.asList("Tom", "Melissa", "Steve", "Megan", "Fernando");
        // Convertimos la lista en un Flux reactivo
        Flux<String> nombresFlux = Flux.fromIterable(listaNombres)
                // PASO 1: filter() - Solo nombres con longitud > 5 letras
                // Melissa y Fernando pasan
                .filter(nombre -> nombre.length() > 5)
                // PASO 2: map() - Transformar a mayúsculas -> MELISSA, FERNANDO
                .map(nombre -> nombre.toUpperCase())
                // PASO 3: log() - Registrar TODOS los eventos (debugging)
                .log();

        // ========== VERIFICACIÓN DE LA PRUEBA ==========
        StepVerifier.create(nombresFlux)
                // Verifica valores esperados
                .expectNext("MELISSA", "FERNANDO")
                // Espera que el flujo complete exitosamente
                .verifyComplete();
    }

    @Test
    public void testTransformUsingFlatMap() {
        List<String> listaNombres = Arrays.asList("Tom", "Melissa", "Steve", "Megan", "Fernando");
        Flux<String> nombresFlux = Flux.fromIterable(listaNombres)
                .filter(nombre -> nombre.length() > 5)
                // En lugar de map() que transforma síncronamente,
                // flatMap() recibe CADA nombre y retorna un Mono<String>
                .flatMap(nombre -> {
                    // Convierte el nombre a mayúsculas y lo envuelve en un Mono
                    return Mono.just(nombre.toUpperCase());
                })
                .log();

        StepVerifier.create(nombresFlux)
                .expectNext("MELISSA", "FERNANDO")
                .verifyComplete();
    }

    @Test
    public void testCombinarFlujosUsandoMerge() {
        // CREAMOS DOS FLUJOS SEPARADOS
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie");
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker");

        // COMBINAMOS CON merge()
        // merge() - SUSCRIBE a AMBOS flujos simultáneamente
        Flux<String> fluxMerge = Flux.merge(flux1, flux2).log();

        // VERIFICAMOS
        StepVerifier.create(fluxMerge)
                .expectSubscription() // Verifica que hubo suscripción
                .expectNext("Blenders", "Old", "Johnnie", "Pride", "Monk", "Walker") // Orden esperado
                .verifyComplete(); // Verifica que todos los flujos completaron
    }

    @Test
    public void testCombinarFlujosUsandoMergeConDemora() {
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie")
                .delayElements(Duration.ofSeconds(1)); // Espera 1 segundo ANTES de emitir CADA elemento
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker")
                .delayElements(Duration.ofSeconds(1)); // Espera 1 segundo ANTES de emitir CADA elemento
        // COMBINAMOS CON merge()
        Flux<String> fluxMerge = Flux.merge(flux1, flux2).log();

        StepVerifier.create(fluxMerge)
                .expectSubscription() // Verifica suscripción
                .expectNextCount(6)   // Espera EXACTAMENTE 6 elementos
                .verifyComplete();    // Verifica que completó
        // ¡NO verificamos elementos específicos porque el orden NO es predecible!
    }

    @Test
    public void testCombinarFlujosConDemoraConOperadorConcat() {
        // ========== 1. CREAMOS FLUJOS CON DEMORA ==========
        // Ambos flujos tienen delay de 1 segundo entre cada elemento
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker")
                .delayElements(Duration.ofSeconds(1));

        // ========== 2. CONCAT - COMBINA SECUENCIALMENTE ==========
        // concat() - ESPERA a que flux1 COMPLETE, SOLO entonces empieza flux2
        // El orden es SIEMPRE: todos los elementos de flux1, luego todos los de flux2
        Flux<String> fluxConcat = Flux.concat(flux1, flux2).log();

        // ========== 3. VERIFICACIÓN ==========
        StepVerifier.create(fluxConcat)
                .expectSubscription()
                // ¡Orden garantizado!
                .expectNext("Blenders", "Old", "Johnnie", "Pride", "Monk", "Walker")
                .verifyComplete();
        // AQUÍ SÍ podemos esperar el orden exacto, aunque haya delays
    }

    @Test
    public void testCombinarFlujosConDemoraConOperadorZip() {
        // ========== 1. CREAMOS LOS FLUJOS ==========
        // Flux1: 3 elementos con delay de 1 segundo
        Flux<String> flux1 = Flux.just("Blenders", "Old", "Johnnie")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker")
                .delayElements(Duration.ofSeconds(1));

        // ========== 2. ZIP - COMBINA EN PAREJAS ==========
        // zip() - Toma un elemento de CADA flujo y los combina
        // Cuando llega (f1, f2), aplica la función para crear un nuevo elemento
        Flux<String> fluxZip = Flux.zip(flux1, flux2, (f1, f2) -> {
            // Combina los dos strings con un espacio en medio
            return f1.concat(" ").concat(f2);
            // "Blenders" + " " + "Pride" = "Blenders Pride"
            // "Old" + " " + "Monk" = "Old Monk"
            // "Johnnie" + " " + "Walker" = "Johnnie Walker"
        }).log();

        // ========== 3. VERIFICACIÓN ==========
        StepVerifier.create(fluxZip)
                .expectNext("Blenders Pride", "Old Monk", "Johnnie Walker")
                .verifyComplete();
        // ¡SALIDA: 3 elementos combinados (NO 6 como en merge/concat)!
    }
}
