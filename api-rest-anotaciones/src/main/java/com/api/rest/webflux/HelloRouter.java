package com.api.rest.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration  // Indica que esta clase define Beans de configuración para Spring
public class HelloRouter {

    @Bean   // Este metodo devuelve un objeto que Spring gestionará como Bean
    public RouterFunction<ServerResponse> functionalRoutes(HelloHandler helloHandler) {
        // Constructor de rutas funcionales, se declaran dos rutas.
        // Para cada ruta le asignamos un metodo
        return RouterFunctions
                .route(RequestPredicates.GET("/functional/mono"), helloHandler::mostrarMensajeMono)
                .andRoute(RequestPredicates.GET("/functional/flux"), helloHandler::mostrarMensajeFlux);
        // Resultado: Se devuelve un objeto RouterFunction que contiene ambas rutas
    }
}
