package com.api.rest.reactiva.repository;

import com.api.rest.reactiva.document.Contacto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ContactoRepository extends ReactiveMongoRepository<Contacto, String> {
    // Busca un contacto por email
    Mono<Contacto> findFirstByEmail(String email);
    // Busca un contacto por telefono o nombre
    Mono<Contacto> findAllByTelefonoOrNombre(String telefonoOrNombre);
}