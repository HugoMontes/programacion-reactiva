package com.manejo.excepciones.ejemplos;

public class CustomException extends Exception {
    public CustomException(String message, Throwable exception) {
        super(message, exception);  // Guarda el mensaje y la causa original
    }
}
