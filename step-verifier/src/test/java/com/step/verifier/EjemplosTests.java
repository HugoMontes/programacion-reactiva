package com.step.verifier;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class EjemplosTests {

    @Test // Indica que este metodo es una prueba unitaria de JUni
    public void testFlux() {
        // 1. CREAMOS EL FLUJO A PROBAR
        // Un Flux simple con 5 números (1,2,3,4,5)
        Flux<Integer> fluxToTest = Flux.just(1, 2, 3, 4, 5);

        // 2. CREAMOS Y EJECUTAMOS LA PRUEBA REACTIVA
        // StepVerifier "se suscribe" al flujo y verifica cada evento
        StepVerifier.create(fluxToTest)    // Paso 1: Crear el verificador con el flujo
                .expectNext(1)          // Espera el primer elemento: 1
                .expectNext(2)          // Espera el segundo elemento: 2
                .expectNext(777)
                .expectNext(4)
                .expectNext(5)
                .expectComplete()          // Espera que el flujo termine exitosamente
                .verify();                 // Dispara la suscripción y valida
        // Si algo no coincide, el test falla
    }

    @Test
    public void testFluxString(){
        // ========== 1. CREAMOS EL FLUJO CON OPERADORES ==========
        // Cadena de transformaciones reactivas:
        // Fuente → filtro → transformación
        Flux<String> fluxToTest = Flux.just(
                        "Jessica",  // 7 letras
                        "John",     // 4 letras ✅
                        "Tomas",    // 5 letras ✅
                        "Melissa",  // 7 letras
                        "Steve",    // 5 letras ✅
                        "Megan",    // 5 letras ✅
                        "Monica",   // 6 letras
                        "Henry"     // 5 letras ✅
                )
                // OPERADOR 1: filter() - Solo mantiene nombres con longitud ≤ 5
                .filter(nombre -> nombre.length() <= 5)
                // OPERADOR 2: map() - Convierte cada nombre a MAYÚSCULAS
                .map(String::toUpperCase);

        // Aquí, NADIE ha ejecutado nada todavía (lazy evaluation)
        // Solo definimos QUÉ hacer, pero NO lo hacemos

        // ========== 2. VERIFICAMOS EL COMPORTAMIENTO =========
        StepVerifier.create(fluxToTest)        // StepVerifier se SUSCRIBE automáticamente
                // 📦 Verificación 1: Valor exacto "JOHN"
                // Procedente de "John" (4 letras) → filtra → toUpperCase() → "JOHN"
                .expectNext("JOHN")
                // 📦 Verificación 2: Valor exacto "TOMAS"
                // Procedente de "Tomas" (5 letras) → filtra → toUpperCase() → "TOMAS"
                .expectNext("TOMAS")
                // 📦 Verificación 3: Verificación por CONDICIÓN (no valor exacto)
                // Procedente de "Steve" (5 letras) → filtra → toUpperCase() → "STEVE"
                // Verificamos que el nombre EMPIECE con "ST" (STEVE cumple ✅)
                .expectNextMatches(nombre -> nombre.startsWith("ST"))
                // 📦 Verificación 4: Valor exacto "MEGAN"
                // Procedente de "Megan" (5 letras) → filtra → toUpperCase() → "MEGAN"
                .expectNext("MEGAN")
                // 📦 Verificación 5: Valor exacto "HENRY"
                // Procedente de "Henry" (5 letras) → filtra → toUpperCase() → "HENRY"
                .expectNext("HENRY")
                .expectComplete()           // Verificación final: El flujo debe completarse exitosamente
                .verify();                  // EJECUTAR la prueba (dispara la suscripción y valida)

        // Si alguna verificación falla, JUnit marcará el test como fallido
    }
}
