package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión integral de rol
 * Maneja operaciones CRUD, asignación de rol
 * y validación de datos para credencial
 */
@Service
public class RolService {

    // REPOSITORIOS INYECTADOS
    @Autowired private RolRepository rolRepository;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todas los roles registradas en el sistema.
     * @return Lista completa de roles
     */
    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }

    /**
     * Busca un rol por su ID único.
     * @param id Identificador del rol
     * @return rol encontrado
     * @throws NoSuchElementException Si no se encuentra el rol
     */
    public Rol findByRol(long id){
        return rolRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró rol con ID: " + id));
    }

    /**
     * Guarda un nuevo rol en el sistema.
     * Realiza validaciones y guarda relaciones con otros componentes.
     * @param rol Datos del rol a guardar
     * @return rol guardado con ID generado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     * @throws DataIntegrityViolationException Si ocurre algún error durante el proceso
     */
    public Rol save(Rol rol) {
        try {

            validarRol(rol);
            return rolRepository.save(rol);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar la rol: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un rol existente.
     * @param rol Datos actualizados del rol
     * @param id Identificador del rol a actualizar
     * @return rol actualizado
     * @throws IllegalArgumentException Si el rol proporcionado es nulo
     * @throws NoSuchElementException Si no se encuentra el rol a actualizar
     * @throws RuntimeException Si ocurre algún error durante la actualización
     */
    public Rol update(Rol rol ,long id) {
        try {
            if (rol == null) {
                throw new IllegalArgumentException("El rol no puede ser nulo");
            }

            Rol antiguaRol = rolRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Rol no encontrada"));

            //Control de errores
            if (rol.getNombre() != null) {
                if (rol.getNombre().length() > 50) {
                    throw new IllegalArgumentException("El Nombre no puede exceder los 50 caracteres");
                } else {
                    antiguaRol.setNombre(rol.getNombre());
                }
            }

            return rolRepository.save(antiguaRol);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el rol: " + e.getMessage());
        }
    }

    /**
     * Elimina un rol del sistema.
     * @param id Identificador del rol a eliminar
     * @throws NoSuchElementException Si no se encuentra el rol
     */
    public void delete(long id){

        if (!rolRepository.existsById(id)) {
            throw new NoSuchElementException("Rol no encontrada");
        }
        rolRepository.deleteById(id);
    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida el rol
     * @param rol rol
     * @throws IllegalArgumentException Si el rol no cumple con las reglas de validación
     */
    public void validarRol(Rol rol) {
        if (rol.getNombre() != null) {
            if (rol.getNombre().length() > 50) {
                throw new RuntimeException("El valor nombre del rol excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El nombre del rol es requerido");
        }
    }

}
