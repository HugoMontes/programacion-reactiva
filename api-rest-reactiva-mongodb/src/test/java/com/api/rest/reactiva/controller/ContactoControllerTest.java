package com.api.rest.reactiva.controller;

import com.api.rest.reactiva.document.Contacto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // Permite ejecutar tests en orden (con @Order)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)        // Crea UNA SOLA instancia para TODOS los tests
public class ContactoControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    // WebTestClient es un cliente HTTP reactivo que permite probar
    // endpoints WebFlux sin necesidad de utilizar Postman o un navegador.

    private Contacto contactoGuardado;

    @Test
    @Order(0)
    public void testGuardarContacto() {
        Flux<Contacto> contactoFlux = webTestClient.post()          // 1. Construye peticion HTTP POST
                .uri("/api/v1/contactos")                       // 2. URL del endpoint
                .accept(MediaType.APPLICATION_JSON)                 // 3. Lo que el cliente ACEPTA como respuesta
                .contentType(MediaType.APPLICATION_JSON)            // 4. Lo que el cliente ENVÍA (Content-Type)
                .body(BodyInserters                                 // 5. Cuerpo de la petición
                        .fromValue(new Contacto(null,"webtest", "w@gmail.com", "992922")))
                .exchange()                                         // 6. Ejecuta la petición
                .expectStatus().isAccepted()                        // 7. Verifica que el status sea 202 ACCEPTED
                .returnResult(Contacto.class)                       // 8. Obtiene el resultado como Flux<Contacto>
                .getResponseBody()                                  // 9. Extrae el cuerpo de la respuesta
                .log();                                             // 10. Imprime el restulado

        // StepVerifier bloquea hasta que el dato llega
        StepVerifier.create(contactoFlux)
                .expectSubscription()                                  // Verifica que exista una suscripción al flujo.
                .expectNextMatches(contacto -> {
                    contactoGuardado = contacto;                       // Guarda el contacto recibido
                    return contacto.getEmail().equals("w@gmail.com");  // Valida que el email sea el esperado
                })
                .verifyComplete();                                     // Verifica que el flujo finalice correctamente

        // Validación tradicional con JUnit.
        // Comprueba que realmente se recibió un objeto Contacto.
        Assertions.assertNotNull(contactoGuardado);
    }

    @Test
    @Order(1)
    public void testObtenerContactoPorEmail() {
        Flux<Contacto> contactoFlux = webTestClient.get()                                  // GET request
                .uri("/api/v1/contactos/byEmail/{email}", "w@gmail.com")    // URL con path variable
                .accept(MediaType.APPLICATION_JSON)                                        // Acepta JSON
                .exchange()                                                                // Ejecuta
                .expectStatus().isOk()                                                     // Espera 200 OK
                .returnResult(Contacto.class).getResponseBody()                            // Deserializa a Contacto
                .log();

        StepVerifier.create(contactoFlux)
                .expectSubscription()
                .expectNextMatches(contacto -> contacto.getEmail().equals("w@gmail.com"))
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void testActualizarContacto() {
        Flux<Contacto> contactoFlux = webTestClient.put()                        // PUT request
                .uri("/api/v1/contactos/{id}", contactoGuardado.getId())     // URL con ID dinámico
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters                                              // Cuerpo con datos actualizados
                        .fromValue(new Contacto(
                                contactoGuardado.getId(),
                                "WebTestClient",
                                "wtc@gmail.com",
                                "11111")))
                .exchange()
                .returnResult(Contacto.class).getResponseBody()
                .log();

        StepVerifier.create(contactoFlux)
                .expectSubscription()
                .expectNextMatches(contacto -> contacto.getEmail().equals("wtc@gmail.com"))
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void testListarContactos() {
        Flux<Contacto> contactosFlux = webTestClient.get()
                .uri("/api/v1/contactos")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Contacto.class).getResponseBody()
                .log();

        StepVerifier.create(contactosFlux)
                .expectSubscription()
                .expectNextCount(1)                         // Espera solo 1 contacto
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void testEliminarContacto() {
        Flux<Void> flux = webTestClient.delete()
                .uri("/api/v1/contactos/{id}", contactoGuardado.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Void.class).getResponseBody();

        StepVerifier.create(flux)
                .expectSubscription()
                .verifyComplete();                          // Solo verifica que completó (Void no emite datos)
    }
}
