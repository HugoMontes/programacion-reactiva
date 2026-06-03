package com.api.rest.reactiva.functional;

import com.api.rest.reactiva.document.Contacto;
import com.api.rest.reactiva.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ContactoHandler {

    @Autowired
    private ContactoRepository contactoRepository;

    // Respuestas por defecto reutilizables (evita repetir código)
    private Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    // Listar contactos
    // Retorna todos los contactos en formato JSON
    public Mono<ServerResponse> listarContactos(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(contactoRepository.findAll(), Contacto.class);
    }

    // Busca un contacto por su ID
    public Mono<ServerResponse> obtenerContactoPorId(ServerRequest request) {
        String id = request.pathVariable("id");
        // Busca y retorna un contacto por su ID
        // flatMap: Si existe, construye respuesta 200
        // switchIfEmpty: Si no existe, retorna 404
        return contactoRepository.findById(id)
                .flatMap(contacto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(contacto)))
                .switchIfEmpty(response404);
    }

    // Busca un contacto por su email (campo único)
    public Mono<ServerResponse> obtenerContactoPorEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        // flatMap: Transforma Mono<Contacto> en Mono<ServerResponse>
        return contactoRepository.findFirstByEmail(email)
                .flatMap(contacto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(contacto)))
                .switchIfEmpty(response404);
    }

    // Insertar un contacto
    public Mono<ServerResponse> insertarContacto(ServerRequest request) {
        // Extrae el cuerpo JSON de la petición como Mono<Contacto>
        Mono<Contacto> contactoMono = request.bodyToMono(Contacto.class);
        // flatMap anidado: Primero guarda, luego construye respuesta
        return contactoMono
                .flatMap(contacto -> contactoRepository.save(contacto)
                        .flatMap(contactoGuardado -> ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(contactoGuardado))))
                .switchIfEmpty(response406);
    }

    // Actualizar contacto
    public Mono<ServerResponse> actualizarContacto(ServerRequest request) {
        // Extrae ID de la ruta
        Mono<Contacto> contactoMono = request.bodyToMono(Contacto.class);
        String id = request.pathVariable("id");
        // Flujo principal: Busca existente → actualiza campos → guarda
        Mono<Contacto> contactoActualizado = contactoMono.flatMap(contacto ->
                contactoRepository.findById(id)
                        .flatMap(oldContacto -> {
                            oldContacto.setTelefono(contacto.getTelefono());
                            oldContacto.setNombre(contacto.getNombre());
                            oldContacto.setEmail(contacto.getEmail());
                            return contactoRepository.save(oldContacto);
                        }));
        // Construye la respuesta HTTP
        return contactoActualizado.flatMap(contacto ->
                        ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(contacto)))
                .switchIfEmpty(response404);
    }

    // Eliminar contacto
    public Mono<ServerResponse> eliminarContacto(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> contactoEliminado = contactoRepository.deleteById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(contactoEliminado, Void.class);
    }
}
