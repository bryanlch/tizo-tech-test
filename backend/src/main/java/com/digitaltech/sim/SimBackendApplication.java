package com.digitaltech.sim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the SIM (Multi-Branch Inventory Management System) Spring Boot application.
 */
@SpringBootApplication
public class SimBackendApplication {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SimBackendApplication.class, args);
    }
}
