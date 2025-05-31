package com.SAFE_Rescue.API_Administrador.repository;

import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de Credenciales
 * Maneja operaciones CRUD desde la base de datos usando Jakarta
 * Maneja validadores para encontrar el correo
 */
@Repository
public interface CredencialRepository extends JpaRepository<Credencial, Long> {

    Credencial findByCorreo(String correo);

    public boolean existsByCorreo(String correo);
}

