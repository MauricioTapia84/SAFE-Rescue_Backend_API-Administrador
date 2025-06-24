package com.SAFE_Rescue.API_Administrador.repository;

import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de Bomberos
 * Maneja operaciones CRUD desde la base de datos usando Jakarta
 * Maneja validadores para encontrar el run y telefono
 */
@Repository
public interface BomberoRepository extends JpaRepository<Bombero , Long> {

    public boolean existsByRun(Integer run);

    public boolean existsByTelefono(Integer telefono);

}