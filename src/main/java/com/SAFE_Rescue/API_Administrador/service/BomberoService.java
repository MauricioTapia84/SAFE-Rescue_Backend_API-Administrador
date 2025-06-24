package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.BomberoRepository;
import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import com.SAFE_Rescue.API_Administrador.repository.CredencialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión integral de ciudadano
 * Maneja operaciones CRUD, asignación de credeniales
 * y validación de datos para ciudadano
 */
@Service
@Transactional
public class BomberoService {

    // REPOSITORIOS INYECTADOS
    @Autowired private BomberoRepository bomberoRepository;
    @Autowired private CredencialRepository credencialRepository;

    // SERVICIOS INYECTADOS
    @Autowired private CredencialService credencialService;


    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todos los Bomberos registrados en el sistema.
     * @return Lista completa de Bomberos
     */
    public List<Bombero> findAll(){
        return bomberoRepository.findAll();
    }

    /**
     * Busca un Bombero por su ID único.
     * @param id Identificador del Bombero
     * @return Bombero encontrado
     * @throws NoSuchElementException Si no se encuentra el Bombero
     */
    public Bombero findByID(long id){
        return bomberoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró Bomberos con ID: " + id));
    }

    /**
     * Guarda un nuevo Bombero en el sistema.
     * Realiza validaciones y guarda relaciones con otros componentes.
     * @param bombero Datos del Bombero a guardar
     * @return Bombero guardado con ID generado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     * @throws DataIntegrityViolationException Si ocurre algún error durante el proceso
     */
    public Bombero save(Bombero bombero) {
        try {

            Credencial guardadaCredencial = credencialService.save(bombero.getCredencial());

            validarBombero(bombero);

            bombero.setCredencial(guardadaCredencial);

            return bomberoRepository.save(bombero);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error: el correo de la credencial ya está en uso.");
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el bombero: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos de un bombero existente.
     * @param bombero Datos actualizados del bombero
     * @param id Identificador del bombero a actualizar
     * @return bombero actualizado
     * @throws IllegalArgumentException Si el bombero proporcionado es nulo
     * @throws NoSuchElementException Si no se encuentra el bombero a actualizar
     * @throws RuntimeException Si ocurre algún error durante la actualización
     */
    public Bombero update(Bombero bombero, long id) {
        try {
            if (bombero == null) {
                throw new IllegalArgumentException("El bombero no puede ser nulo");
            }

            Bombero antiguoBombero = bomberoRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Bombero no encontrado"));

            //Control de errores
            if (bombero.getNombre() != null) {
                if (bombero.getNombre().length() > 50) {
                    throw new RuntimeException("El valor nombre excede máximo de caracteres (50)");
                }else {
                    antiguoBombero.setNombre(bombero.getNombre());
                }
            }

            if (bombero.getTelefono() != null) {
                if (bomberoRepository.existsByTelefono(bombero.getTelefono())) {
                    throw new RuntimeException("El Telefono ya existe");
                }else{
                    if (String.valueOf(bombero.getTelefono()).length()> 9) {
                        throw new RuntimeException("El valor telefono excede máximo de caracteres (9)");
                    }else {
                        antiguoBombero.setTelefono(bombero.getTelefono());
                    }
                }
            }

            if (bombero.getRun() != null) {
                if (bomberoRepository.existsByRun(bombero.getRun())) {
                    throw new RuntimeException("El RUN ya existe");
                }else{
                    if (String.valueOf(bombero.getRun()).length() > 8) {
                        throw new RuntimeException("El valor RUN excede máximo de caracteres (8)");
                    }else {
                        antiguoBombero.setRun(bombero.getRun());
                    }
                }
            }

            if (bombero.getDv() != null) {
                if (bombero.getDv().length() > 1) {
                    throw new RuntimeException("El valor DV excede máximo de caracteres (1)");
                }else {
                    antiguoBombero.setDv(bombero.getDv());
                }
            }

            if (bombero.getAPaterno() != null) {
                if (bombero.getAPaterno().length() > 50) {
                    throw new RuntimeException("El valor a_paterno excede máximo de caracteres (50)");
                }else {
                    antiguoBombero.setAPaterno(bombero.getAPaterno());
                }
            }

            if (bombero.getAMaterno() != null) {
                if (bombero.getAMaterno().length() > 50) {
                    throw new RuntimeException("El valor a_materno excede máximo de caracteres (50)");
                }else {
                    antiguoBombero.setAMaterno(bombero.getAMaterno());
                }
            }

            if (bombero.getFechaRegistro() != null) {
                antiguoBombero.setFechaRegistro(bombero.getFechaRegistro());
            }


            return bomberoRepository.save(antiguoBombero);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el bombero: " + e.getMessage());
        }
    }

    /**
     * Elimina un bombero del sistema.
     * @param id Identificador del bombero a eliminar
     * @throws NoSuchElementException Si no se encuentra el bombero
     */
    public void delete(long id){

        if (!bomberoRepository.existsById(id)) {
            throw new NoSuchElementException("Bombero no encontrado");
        }
        bomberoRepository.deleteById(id);

    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida el bombero
     * @param bombero bombero
     * @throws IllegalArgumentException Si el bombero no cumple con las reglas de validación
     */
    public void validarBombero(@NotNull Bombero bombero) {

        if (bombero.getRun() >= 0) {
            throw new IllegalArgumentException("La Cantidad debe ser un número positivo");
        } else {
            if (String.valueOf(bombero.getRun()).length() > 8) {
                throw new RuntimeException("El valor RUN excede máximo de caracteres (8)");
            }else{
                if (bomberoRepository.existsByRun(bombero.getRun())) {
                    throw new RuntimeException("El RUN ya existe");
                }
            }
        }

        if (bombero.getDv() != null) {
            if (bombero.getDv().length() > 1) {
                throw new RuntimeException("El valor DV excede máximo de caracteres (1)");
            }
        } else {
            throw new IllegalArgumentException("El DV del bombero es requerido");
        }

        if (bombero.getNombre() != null) {
            if (bombero.getNombre().length() > 50) {
                throw new RuntimeException("El valor nombre del bombero excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El nombre del bombero es requerido");
        }

        if (bombero.getAPaterno() != null) {
            if (bombero.getAPaterno().length() > 50) {
                throw new RuntimeException("El valor apellido paterno del bombero excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El apellido paterno  del bombero es requerido");
        }

        if (bombero.getAMaterno() != null) {
            if (bombero.getAMaterno().length() > 50) {
                throw new RuntimeException("El valor apellido materno excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El apellido materno  del ciudadano es requerido");
        }

        if (bombero.getTelefono() >= 0) {
            throw new IllegalArgumentException("La Cantidad debe ser un número positivo");
        } else {
            if (String.valueOf(bombero.getTelefono()).length()> 9) {
                throw new RuntimeException("El valor telefono excede máximo de caracteres (9)");
            }else{
                if (bomberoRepository.existsByTelefono(bombero.getTelefono())) {
                    throw new RuntimeException("El Telefono ya existe");
                }
            }
        }

    }

    // MÉTODOS DE ASIGNACIÓN DE RELACIONES

    /**
     * Asigna un Credencial a un bombero
     * @param bomberoId ID del bombero
     * @param credencialId ID del credencial
     */
    public void asignarCredencial(long bomberoId, long credencialId) {
        Bombero bombero = bomberoRepository.findById(bomberoId)
                .orElseThrow(() -> new RuntimeException("Bombero no encontrado"));

        Credencial credencial = credencialRepository.findById(credencialId)
                .orElseThrow(() -> new RuntimeException("Credencial no encontrada"));

        bombero.setCredencial(credencial);
        bomberoRepository.save(bombero);
    }

}