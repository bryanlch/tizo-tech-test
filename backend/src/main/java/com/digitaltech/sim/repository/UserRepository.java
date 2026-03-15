package com.digitaltech.sim.repository;

import com.digitaltech.sim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad de Usuario.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca usuario por su username.
     * 
     * @param username a buscar
     * @return Optional del usuario
     */
    Optional<User> findByUsername(String username);

    /**
     * Revisa si el username existe.
     * 
     * @param username a buscar
     * @return booleano true si ya existe
     */
    boolean existsByUsername(String username);
}
