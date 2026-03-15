package com.digitaltech.sim.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@digitaltech.com";
        boolean userExists = checkUserExists(adminEmail);
        
        if (userExists) {
            return; 
        }
        
        System.out.println("Generando el primer usuario administrador de forma automática...");
    }

    private boolean checkUserExists(String email) {
        return false; 
    }
}