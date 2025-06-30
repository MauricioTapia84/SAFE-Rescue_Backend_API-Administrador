package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Roles", description = "Operaciones de CRUD relacionadas con Roles")
public class RolController {

    @Autowired
    private RolService rolService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los roles registrados en el sistema.
     * @return ResponseEntity con lista de roles o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    @Operation(summary = "Obtener todos los roles", description = "Obtiene una lista con todos los roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Rol.class))),
            @ApiResponse(responseCode = "204", description = "No hay roles registrados.")
    })
    public ResponseEntity<List<Rol>> listar() {
        List<Rol> roles = rolService.findAll();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(roles);
    }

    /**
     * Busca un rol por su ID.
     * @param id ID del rol a buscar
     * @return ResponseEntity con el rol encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un rol por su ID", description = "Obtiene un rol al buscarlo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Rol.class))),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado.")
    })
    public ResponseEntity<?> buscarRol(@Parameter(description = "ID del rol a buscar", required = true)
                                       @PathVariable int id) {
        Rol rol;
        try {
            rol = rolService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Rol no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(rol);
    }

    /**
     * Crea un nuevo rol.
     * @param rol Datos del rol a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo rol", description = "Crea un nuevo rol en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado con éxito."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> agregarRol(@RequestBody @Parameter(description = "Datos del rol a crear", required = true)
                                             Rol rol) {
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
     * Actualiza un rol existente.
     * @param id ID del rol a actualizar
     * @param rol Datos actualizados del rol
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un rol existente", description = "Actualiza los datos de un rol por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado con éxito."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> actualizarRol(@Parameter(description = "ID del rol a actualizar", required = true)
                                                @PathVariable Integer id,
                                                @RequestBody @Parameter(description = "Datos actualizados del rol", required = true)
                                                Rol rol) {
        try {
            rolService.update(rol, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un rol del sistema.
     * @param id ID del rol a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rol", description = "Elimina un rol del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol eliminado con éxito."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> eliminarRol(@Parameter(description = "ID del rol a eliminar", required = true)
                                              @PathVariable Integer id) {
        try {
            rolService.delete(id);
            return ResponseEntity.ok("Rol eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}