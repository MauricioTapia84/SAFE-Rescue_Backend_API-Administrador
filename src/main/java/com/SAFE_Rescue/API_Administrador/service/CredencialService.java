package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.CredencialRepository;
import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión integral de credencial
 * Maneja operaciones CRUD, asignación de credeniales
 * y validación de datos para credencial
 */
@Service
@Transactional
public class CredencialService {

    // REPOSITORIOS INYECTADOS
    @Autowired private CredencialRepository credencialRepository;
    @Autowired private RolRepository rolRepository;

    // SERVICIOS INYECTADOS
    @Autowired private RolService rolService;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todas las credenciales registradas en el sistema.
     * @return Lista completa de credenciales
     */
    public List<Credencial> findAll(){
        return credencialRepository.findAll();
    }

    /**
     * Busca una credencial por su ID único.
     * @param id Identificador del credencial
     * @return credencial encontrado
     * @throws NoSuchElementException Si no se encuentra el credencial
     */
    public Credencial findByID(Integer id){
        return credencialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró Credencial con ID: " + id));
    }


    /**
     * Guarda un nuevo credencial en el sistema.
     * Realiza validaciones y guarda relaciones con otros componentes.
     * @param credencial Datos del credencial a guardar
     * @return credencial guardado con ID generado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     * @throws DataIntegrityViolationException Si ocurre algún error durante el proceso
     */
    public Credencial save(Credencial credencial) {
        try {
            Rol rol = rolService.save(credencial.getRol());

            credencial.setRol(rol);

            validarCredencial(credencial);
            return credencialRepository.save(credencial);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("El correo ya está en uso. Por favor, use otro.");
        } catch (EntityNotFoundException f) {
            throw new RuntimeException("Error al guardar la credencial: " + f.getMessage());
        } catch (Exception g) {
            throw new RuntimeException("Error inesperado: " + g.getMessage());
        }
    }

    /**
     * Actualiza los datos de un credencial existente.
     * @param credencial Datos actualizados del credencial
     * @param id Identificador del credencial a actualizar
     * @return credencial actualizado
     * @throws IllegalArgumentException Si el credencial proporcionado es nulo
     * @throws NoSuchElementException Si no se encuentra el credencial a actualizar
     * @throws RuntimeException Si ocurre algún error durante la actualización
     */
    public Credencial update(Credencial credencial ,Integer id) {
        try {
            if (credencial == null) {
                throw new IllegalArgumentException("El Credencial no puede ser nulo");
            }

            Credencial antiguaCredencial = credencialRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Credencial no encontrada"));

            //Control de errores
            if (credencial.getContrasenia() != null) {
                if (credencial.getContrasenia().length() > 16) {
                    throw new RuntimeException("El valor contrasenia excede máximo de caracteres (16)");
                }else {
                    antiguaCredencial.setContrasenia(credencial.getContrasenia());
                }
            }

            if (credencial.getCorreo() != null) {
                if (credencialRepository.existsByCorreo(credencial.getCorreo())) {
                    throw new RuntimeException("El Correo ya existe");
                }else{
                    if (credencial.getCorreo().length() > 80) {
                        throw new RuntimeException("El valor correo excede máximo de caracteres (80)");
                    }else {
                        antiguaCredencial.setCorreo(credencial.getCorreo());
                    }
                }
            }

            antiguaCredencial.setActivo(credencial.isActivo());
            return credencialRepository.save(antiguaCredencial);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al actualizar la credencial: " + e.getMessage());
        } catch (NoSuchElementException  f) {
            throw new NoSuchElementException("Error al actualizar la credencial: " + f.getMessage());
        } catch (Exception g) {
            throw new RuntimeException("Error al actualizar la credencial: " + g.getMessage());
        }
    }

    /**
     * Elimina un credencial del sistema.
     * @param id Identificador del credencial a eliminar
     * @throws NoSuchElementException Si no se encuentra el credencial
     */
    public void delete(Integer id){

        if (!credencialRepository.existsById(id)) {
            throw new NoSuchElementException("Credencial no encontrada");
        }
        credencialRepository.deleteById(id);
    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida la credencial
     * @param credencial credencial
     * @throws IllegalArgumentException Si la credencial no cumple con las reglas de validación
     */
    public void validarCredencial(Credencial credencial) {

        if (credencial.getContrasenia() == null) {
            throw new IllegalArgumentException("La Contrasenia del ciudadano es requerido");
        }

        if (credencial.getContrasenia().length() > 16) {
            throw new IllegalArgumentException("El valor Contrasenia excede máximo de caracteres (16)");
        }

        if (credencial.getCorreo() == null) {
            throw new IllegalArgumentException("El Correo es requerido");
        }

        if (credencial.getCorreo().length() >80) {
            throw new IllegalArgumentException("El valor de Correo excede máximo de caracteres (80)");
        }

        if (credencial.getIntentosFallidos() < 0) {
            throw new IllegalArgumentException("La Cantidad debe ser un número positivo");
        }

        rolService.validarRol(credencial.getRol());
    }

    // MÉTODOS DE VERIFICACION DE CREDENCIALES PARA LOGIN

    /**
     * Verifica la contraseña al iniciar sesión
     * @param correo correo del ciudadano
     * @param contrasenia contrasenia del ciudadano
     */
    public boolean verificarCredenciales(String correo, String contrasenia) {
        Credencial credencial = credencialRepository.findByCorreo(correo);
        if (credencial != null) {
            boolean sonCorrectas = contrasenia.equals(credencial.getContrasenia());
            if (!sonCorrectas) {
                credencial.setIntentosFallidos(credencial.getIntentosFallidos() + 1);
                credencialRepository.save(credencial);
            }
            return sonCorrectas;
        }
        return false;
    }

    // MÉTODOS DE ASIGNACIÓN DE RELACIONES

    /**
     * Asigna un rol  a una Credencial
     * @param credencialId ID del credencial
     * @param  rolId del rol
     */
    public void asignarRol(Integer credencialId,Integer rolId) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Credencial credencial = credencialRepository.findById(credencialId)
                .orElseThrow(() -> new RuntimeException("Credencial no encontrada"));

        credencial.setRol(rol);
        credencialRepository.save(credencial);
    }

}
