package com.thymeleaf.reactivo;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepository {

    // ========== 1. FUENTES DE DATOS ESTÁTICAS ==========
    private static List<Producto> telefonos = new ArrayList<>();
    private static List<Producto> televisores = new ArrayList<>();

    // ========== 2. BLOQUE ESTÁTICO DE INICIALIZACIÓN ==========
    // Se ejecuta UNA SOLA vez cuando la clase se carga en memoria
    // Llena las listas con datos de ejemplo
    static {
        telefonos.add(new Producto(1, "iPhone", 200));
        telefonos.add(new Producto(2, "Honor", 300));
        telefonos.add(new Producto(3, "Samsung", 200));

        televisores.add(new Producto(4, "LG", 200));
        televisores.add(new Producto(5, "Sony", 30));
        televisores.add(new Producto(6, "TCL", 20));
    }

    // ========== 3. MÉTODOS REACTIVOS ==========
    // Metodo 1: Buscar todos los productos de la lista1
    // IMPORTANTE: NO bloquea el hilo, devuelve Flux reactivo
    // delayElements: cada elemento tarda 3 segundos en emitirse (simula latencia de BD)
    public Flux<Producto> buscarTelefonos() {
        // Convierte la lista tradicional en un Flux reactivo
        // delayElements simula una operación lenta (base de datos, API externa)
        return Flux.fromIterable(telefonos)
                .delayElements(Duration.ofSeconds(3)); // 3 segundos entre cada elemento
        // Tiempo total: 9 segundos para emitir los 3 productos (3s + 3s + 3s)
    }

    // Metodo 2: Buscar todos los productos de la lista2
    // Similar al anterior pero con diferentes datos
    public Flux<Producto> buscarTelevisores() {
        return Flux.fromIterable(televisores).delayElements(Duration.ofSeconds(3));
        // Tiempo total: 9 segundos también
    }
}
