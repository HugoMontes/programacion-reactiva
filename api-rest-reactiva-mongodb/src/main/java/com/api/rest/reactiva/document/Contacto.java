package com.api.rest.reactiva.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data                    // Genera: getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Constructor sin argumentos: Contacto()
@AllArgsConstructor      // Constructor con todos los argumentos
@Document(collection = "contacto")
public class Contacto {
    @Id
    private String id;
    private String nombre;
    private String email;
    private String telefono;
}
