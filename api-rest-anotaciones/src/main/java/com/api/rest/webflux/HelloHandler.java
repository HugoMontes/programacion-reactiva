package com.api.rest.webflux;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class HelloHandler {

    // ========== METODO 1: MANEJADOR PARA MONO ==========
    // Procesar peticiones que devuelven UN SOLO mensaje
    public Mono<ServerResponse> mostrarMensajeMono(ServerRequest serverRequest) {
        return ServerResponse.ok()                              // Construye respuesta HTTP 200 (OK)
                .contentType(MediaType.TEXT_PLAIN)              // La respuesta será texto plano
                .body(
                        Mono.just("Hola mensaje Mono"),    // crea un publicador con un solo valor
                        String.class                            // El tipo del dato que se va a serializar
                );
        // Resultado: El cliente recibe "Hola mensaje Mono" como texto plano
    }

    // ========== METODO 2: MANEJADOR PARA FLUX (STREAMING) ==========
    // Procesar peticiones que devuelven UN STREAM de datos
    public Mono<ServerResponse> mostrarMensajeFlux(ServerRequest serverRequest) {
        return ServerResponse.ok()
                // Esto indica que la respuesta será un stream de eventos SSE
                .contentType(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .body(
                        Flux.just("Hola", "mensaje", "Flux")    // Crea un publicador con MÚLTIPLES valores
                                .delayElements(Duration.ofSeconds(1))  // Cada elemento se emite con 1 segundo de delay
                        , String.class
                );
        // Resultado: El cliente recibe:
        //   t=1s: "Hola"
        //   t=2s: "mensaje"
        //   t=3s: "Flux"
        //   luego se cierra la conexión
    }
}
