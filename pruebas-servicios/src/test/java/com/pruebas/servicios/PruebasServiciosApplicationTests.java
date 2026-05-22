package com.pruebas.servicios;

import com.pruebas.servicios.service.SimpleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class PruebasServiciosApplicationTests {

	@Autowired
	SimpleService servicioSencillo;   // Inyecta el servicio real (no un mock)

	@Test
	void testMono(){
		// 1 LLAMADA AL SERVICIO (NO BLOQUEANTE)
		// El metodo devuelve INMEDIATAMENTE un Mono<String>
		// NO espera a que el dato esté listo
		Mono<String> uno = servicioSencillo.buscarUno();

		// 2 VERIFICACIÓN REACTIVA
		// StepVerifier se SUSCRIBE al Mono y verifica los eventos
		StepVerifier.create(uno)
				.expectNext("hola")   // Espera que emita "hola"
				.verifyComplete();      // Espera que complete exitosamente
	}

	@Test
	void testVarios(){
		// 1. LLAMADA AL SERVICIO
		Flux<String> varios = servicioSencillo.buscarTodos();

		// 2. VERIFICACIÓN DE CADA ELEMENTO
		StepVerifier.create(varios)
				.expectNext("hola")     // 1er elemento
				.expectNext("que")      // 2do elemento
				.expectNext("tal")      // 3er elemento
				.expectNext("estas")    // 4to elemento
				.verifyComplete();      // Verifica que termina

		// Los elementos se verifican en el ORDEN exacto de emisión
	}

	// ========== TEST 3: FLUX CON RETRASOS (TIEMPO REAL) ==========
	@Test
	void testVariosLenta(){
		// 1. SERVICIO CON delaySequence() - CADA elemento tiene retraso
		Flux<String> varios = servicioSencillo.buscarTodosLento();

		// 2. VERIFICACIÓN CON ESPERAS EXPLÍCITAS
		StepVerifier.create(varios)
				.expectNext("hola")               // Espera "hola"
				.thenAwait(Duration.ofSeconds(1))   // Espera 1 segundo REAL
				.expectNext("que")                // Luego "que"
				.thenAwait(Duration.ofSeconds(1))   // Espera otro segundo
				.expectNext("tal")                // Luego "tal"
				.thenAwait(Duration.ofSeconds(1))   // Espera otro segundo
				.expectNext("estas")              // Luego "estas"
				.thenAwait(Duration.ofSeconds(1))   // Espera otro segundo
				.verifyComplete();                  // Verifica finalización

		// Este test TARDA aproximadamente 4 segundos en ejecutarse
		// (1 segundo entre cada elemento)
	}
}
