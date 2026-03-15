package com.digitaltech.sim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal para arrancar la aplicacion Spring Boot del SIM.
 */
@SpringBootApplication
public class SimBackendApplication {

    /**
     * Metodo principal.
     * @param args Argumentos de linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(SimBackendApplication.class, args);
    }
}
