package com.project.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class TestUsuario {
    static void main() {

        // ========== 1. FUENTE DE DATOS (PUBLISHER) ==========
        // Flux con 6 nombres completos
        Flux<String> nombres = Flux.just(
                "Juan Lopez",
                "Pedro Gonzales",
                "Jhon Smith",
                "Mateo Robles",
                "Ana Hernandez",
                "Sophie Smith");

        // ========== 2. PIPELINE DE OPERADORES ==========
        // CADA operador crea un NUEVO Flux transformado
        Flux<Usuario> usuarios = nombres
                // OPERADOR 1: map() - Transforma String → Usuario
                // Toma cada nombre, lo divide en nombre/apellido y crea un Usuario
                .map(nombre -> new Usuario(
                        nombre.split(" ")[0].toUpperCase(),     // Nombre en mayúsculas
                        nombre.split(" ")[1].toUpperCase()))    // Apellido en mayúsculas
                // OPERADOR 2: filter() - Filtra elementos
                // SOLO deja pasar los usuarios con apellido "Smith" (sin importar mayúsculas)
                .filter(usuario -> usuario.getApellido().equalsIgnoreCase("Smith"))
                // OPERADOR 3: doOnNext() - Acción secundaria (side-effect)
                // NO transforma el dato, solo permite hacer algo (como logging o validación)
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Los nombres no pueden estar vacios");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })
                // OPERADOR 4: map() - Transforma el Usuario (modifica el nombre a minúsculas)
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        // ========== 3. SUSCRIPCIÓN (EJECUCIÓN) ==========
        // ¡NADA pasa hasta aquí! El flujo solo se ejecuta cuando alguien se suscribe
        usuarios.subscribe(
                e -> log.info(e.toString()),                    // onNext: qué hacer con cada dato
                error -> log.error(error.getMessage()),       // onError: qué hacer si hay error
                new Runnable() {                                        // onComplete: qué hacer al terminar
                    @Override
                    public void run() {
                        log.info("Se ha finalizado la ejecucion del observable con exito.");
                    }
                });
    }
}
