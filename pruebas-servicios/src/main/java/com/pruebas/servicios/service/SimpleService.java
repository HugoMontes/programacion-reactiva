package com.pruebas.servicios.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class SimpleService {

    // ========== METODO 1: MONO (0-1 elemento) ==========
    // Los servicios reactivos NO devuelven el dato crudo
    // Devuelven un "contenedor reactivo" (Mono/Flux)
    public Mono<String> buscarUno(){
        return Mono.just("hola");
    }

    // ========== METODO 2: FLUX (múltiples elementos) ==========
    // Flux para colecciones/streams de datos
    public Flux<String> buscarTodos(){
        return Flux.just("hola","que","tal","estas");
        // El cliente recibirá 4 eventos onNext y luego onComplete
    }

    // ========== METODO 3: FLUX CON RETRASO ==========
    // Simular operaciones lentas (BD, API externa)
    public Flux<String> buscarTodosLento(){
        // Crea el mismo Flux pero con un RETRASO de 10 segundos ANTES de emitir el primer elemento
        return Flux.just("hola","que","tal","estas").delaySequence(Duration.ofSeconds(10));
        // IMPORTANTE: NO bloquea el hilo durante 10 segundos
        // Simplemente programa que la emisión empiece después de 10 segundos
        // El hilo queda LIBRE para atender otras peticiones
    }
}
