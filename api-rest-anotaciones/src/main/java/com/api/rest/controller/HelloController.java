package com.api.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/hello")
public class HelloController {

    // ========== ENDPOINT 1: MONO (0-1 elemento) ==========
    @GetMapping("/mono")
    public Mono<String> getMono() {
        // Endpoint que retorna UN SOLO elemento
        return Mono.just("Hola desde Mono");
        // WebFlux serializa la respuesta automáticamente a JSON: "Hola desde Mono"
    }

    // ========== ENDPOINT 2: FLUX (múltiples elementos) ==========
    // Le dice al navegador/cliente: "Prepárate para recibir un STREAM de datos en tiempo real"
    @GetMapping(path = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getFlux() {
        // Endpoint que retorna UN STREAM de datos
        Flux<String> mensaje = Flux.just("Hola ", "desde ", "Flux")
                .delayElements(Duration.ofSeconds(1)) // Añadir delay entre cada elemento (simula operación lenta)
                .log();                               // Log para debugging
        return mensaje;
    }
}
