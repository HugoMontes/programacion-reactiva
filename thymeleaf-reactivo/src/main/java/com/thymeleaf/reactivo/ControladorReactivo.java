package com.thymeleaf.reactivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

@Controller // Anotación Spring: esta clase es un controlador web MVC
public class ControladorReactivo {

    @Autowired // Inyección de dependencia: Spring nos da el repositorio
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    // ========== ENDPOINT REACTIVO CON DATA DRIVER ==========
    @RequestMapping("/lista")
    public String listarProductos(Model model) {
        // PASO 1: Crear variable reactiva con Data Driver
        IReactiveDataDriverContextVariable listaReactiva =
                new ReactiveDataDriverContextVariable(
                        productoRepository.buscarTelefonos(),    // Flux<Producto> - fuente reactiva
                        1);                                      // Tamaño del buffer (chunk size)
        // El número '1' indica que la plantilla procesará de 1 en 1 elemento

        // PASO 2: Agregar la variable reactiva al modelo y retornar el nombre de la plantilla
        model.addAttribute("listaProductos", listaReactiva);
        return "lista";
    }

    // ========== ENDPOINT REACTIVO CON DOS FLUJOS ==========
    @RequestMapping("/lista/todos")
    public String listarTodos(Model model) {
        // PASO 1: Crear variable reactiva con Data Driver
        IReactiveDataDriverContextVariable listaReactiva =
                new ReactiveDataDriverContextVariable(
                        productoService.buscarTodos(),    // Flux<Producto> - fuente reactiva
                        1);                               // Tamaño del buffer (chunk size)
        // El número '1' indica que la plantilla procesará de 1 en 1 elemento

        // PASO 2: Agregar la variable reactiva al modelo y retornar el nombre de la plantilla
        model.addAttribute("listaProductos", listaReactiva);
        return "lista";
    }
}
