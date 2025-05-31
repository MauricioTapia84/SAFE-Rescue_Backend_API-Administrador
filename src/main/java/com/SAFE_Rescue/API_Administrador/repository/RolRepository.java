package com.SAFE_Rescue.API_Administrador.repository;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de roles
 * Maneja operaciones CRUD desde la base de datos usando Jakarta
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

}
