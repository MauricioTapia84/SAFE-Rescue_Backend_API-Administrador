package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Login;
import com.SAFE_Rescue.API_Administrador.service.CredencialService;
import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de credenciales
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de credenciales
 */
@RestController
@RequestMapping("/api-administrador/v1/credenciales")
public class CredencialController {

    // SERVICIOS INYECTADOS

    @Autowired
    private CredencialService credencialService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos las credenciales registradas en el sistema.
     * @return ResponseEntity con lista de credenciales o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Credencial>> listar(){

        List<Credencial> credenciales = credencialService.findAll();
        if(credenciales.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(credenciales);
    }

    /**
     * Busca un Credencial por su ID.
     * @param id ID del Credencial a buscar
     * @return ResponseEntity con el Credencial encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCredencial(@PathVariable long id) {
        Credencial credencial;

        try {
            credencial = credencialService.findByID(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Credencial no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(credencial);
    }

    /**
     * Crea una nueva Credencial
     * @param credencial Datos de la Credencial a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarCredencial(@RequestBody Credencial credencial) {
        try {
            credencialService.save(credencial);
            return ResponseEntity.status(HttpStatus.CREATED).body("Credencial creada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza una Credencial existente.
     * @param id ID de la Credencial a actualizar
     * @param credencial Datos actualizados de la Credencial
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCredencial(@PathVariable long id, @RequestBody Credencial credencial) {
        try {
            Credencial nuevoCredencial = credencialService.update(credencial, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Credencial no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una credencial del sistema.
     * @param id ID del credencial a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCredencial(@PathVariable long id) {
        try {
            credencialService.delete(id);
            return ResponseEntity.ok("Credencial eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Credencial no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Permite iniciar sesion
     * @param login credenciales de inicio sesion
     * @return ResponseEntity con mensaje de confirmación o error y aumenta la cantidad de intentos fallidos
     */    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        boolean isAuthenticated = credencialService.verificarCredenciales(login.getCorreo(), login.getContrasenia());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    /**
     * Asigna un rol a una credencial
     * @param credencialId ID del credencial
     * @param rolId ID del rol a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{credencialId}/asignar-rol/{rolId}")
    public ResponseEntity<String> asignarRol(@PathVariable int credencialId,@PathVariable int rolId) {
        try {
            credencialService.asignarRol(credencialId,rolId);
            return ResponseEntity.ok("Rol asignada al credencial exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
