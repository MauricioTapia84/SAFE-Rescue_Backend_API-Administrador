package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.service.BomberoService;
import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de Bomberos
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de Bomberos
 */
@RestController
@RequestMapping("/api-administrador/v1/bomberos")
public class BomberoController {

    // SERVICIOS INYECTADOS

    @Autowired
    private BomberoService bomberoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los bomberos registrados en el sistema.
     * @return ResponseEntity con lista de bomberos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Bombero>> listar(){

        List<Bombero> bomberos = bomberoService.findAll();
        if(bomberos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(bomberos);
    }

    /**
     * Busca un bombero bpor su ID.
     * @param id ID del bombero a buscar
     * @return ResponseEntity con el bombero encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarBombero(@PathVariable long id) {
        Bombero bombero;

        try {
            bombero = bomberoService.findByID(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Bombero no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bombero);
    }

    /**
     * Crea un nuevo Bombero
     * @param bombero Datos del Bombero a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarBombero(@RequestBody Bombero bombero) {
        try {
            Bombero nuevoBombero = bomberoService.save(bombero);
            return ResponseEntity.status(HttpStatus.CREATED).body("Bombero creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un Bombero existente.
     * @param id ID del Bombero a actualizar
     * @param bombero Datos actualizados del Bombero
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarBombero(@PathVariable long id, @RequestBody Bombero bombero) {
        try {
            Bombero nuevoBombero = bomberoService.update(bombero, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bombero no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un Bombero del sistema.
     * @param id ID del Bombero a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBombero(@PathVariable long id) {

        try {
            bomberoService.delete(id);
            return ResponseEntity.ok("Bombero eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bombero no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    // GESTIÓN DE RELACIONES

    /**
     * Asigna una credencial a un bombero
     * @param bomberoId ID del bombero
     * @param credencialId ID de la credencial a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{bomberoId}/asignar-credencial/{credencialId}")
    public ResponseEntity<String> asignarCredencial(@PathVariable int bomberoId, @PathVariable int credencialId) {
        try {
            bomberoService.asignarCredencial(bomberoId, credencialId);
            return ResponseEntity.ok("Credencial asignada al bombero exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}