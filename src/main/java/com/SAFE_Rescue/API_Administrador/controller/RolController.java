package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de roles
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de roles
 */
@RestController
@RequestMapping("/api-administrador/v1/roles")
public class RolController {

    // SERVICIOS INYECTADOS

    @Autowired private RolService rolService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos las roles registradas en el sistema.
     * @return ResponseEntity con lista de roles o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles(){

        List<Rol> rol = rolService.findAllRoles();
        if(rol.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(rol);
    }

    /**
     * Busca un rol por su ID.
     * @param id ID del rol a buscar
     * @return ResponseEntity con el rol encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarRol(@PathVariable int id) {
        Rol rol;

        try {
            rol = rolService.findByRol(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Rol no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(rol);
    }

    /**
     * Crea una nuevo rol
     * @param rol Datos del rol a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarRol(@RequestBody Rol rol) {
        try {
            rolService.save(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rol creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza una rol existente.
     * @param id ID de la rol a actualizar
     * @param rol Datos actualizados de la rol
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRol(@PathVariable long id, @RequestBody Rol rol) {
        try {
            Rol nuevoRol = rolService.update(rol, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Rol no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una rol del sistema.
     * @param id ID del rol a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRol(@PathVariable long id) {
        try {
            rolService.delete(id);
            return ResponseEntity.ok("Rol eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Rol no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

}