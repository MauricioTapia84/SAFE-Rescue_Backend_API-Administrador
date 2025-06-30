package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Login;
import com.SAFE_Rescue.API_Administrador.service.CredencialService;
import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
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
 * Controlador REST para la gestión de credenciales
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de credenciales
 */
@RestController
@RequestMapping("/api-administrador/v1/credenciales")
@Tag(name = "Credenciales", description = "Operaciones de CRUD relacionadas con credenciales, métodos de inicio de sesión y asignación de roles")
public class CredencialController {

    @Autowired
    private CredencialService credencialService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todas las credenciales registradas en el sistema.
     * @return ResponseEntity con lista de credenciales o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    @Operation(summary = "Obtener todas las credenciales", description = "Obtiene una lista con todas las credenciales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de credenciales obtenida exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credencial.class))),
            @ApiResponse(responseCode = "204", description = "No hay credenciales registradas.")
    })
    public ResponseEntity<List<Credencial>> listar() {
        List<Credencial> credenciales = credencialService.findAll();
        if (credenciales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(credenciales);
    }

    /**
     * Busca una credencial por su ID.
     * @param id ID de la credencial a buscar
     * @return ResponseEntity con la credencial encontrada o mensaje de error
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una credencial por su ID", description = "Obtiene una credencial al buscarla por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credencial encontrada.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Credencial.class))),
            @ApiResponse(responseCode = "404", description = "Credencial no encontrada.")
    })
    public ResponseEntity<?> buscarCredencial(@Parameter(description = "ID de la credencial a buscar", required = true)
                                              @PathVariable Integer id) {
        Credencial credencial;
        try {
            credencial = credencialService.findByID(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Credencial no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(credencial);
    }

    /**
     * Crea una nueva credencial.
     * @param credencial Datos de la credencial a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    @Operation(summary = "Crear una nueva credencial", description = "Crea una nueva credencial en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Credencial creada con éxito."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> agregarCredencial(@RequestBody @Parameter(description = "Datos de la credencial a crear", required = true)
                                                    Credencial credencial) {
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
     * Actualiza una credencial existente.
     * @param id ID de la credencial a actualizar
     * @param credencial Datos actualizados de la credencial
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una credencial existente", description = "Actualiza los datos de una credencial por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credencial actualizada con éxito."),
            @ApiResponse(responseCode = "404", description = "Credencial no encontrada."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> actualizarCredencial(@Parameter(description = "ID de la credencial a actualizar", required = true)
                                                       @PathVariable Integer id,
                                                       @RequestBody @Parameter(description = "Datos actualizados de la credencial", required = true)
                                                       Credencial credencial) {
        try {
            credencialService.update(credencial, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una credencial del sistema.
     * @param id ID de la credencial a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una credencial", description = "Elimina una credencial del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credencial eliminada con éxito."),
            @ApiResponse(responseCode = "404", description = "Credencial no encontrada."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<String> eliminarCredencial(@Parameter(description = "ID de la credencial a eliminar", required = true)
                                                     @PathVariable Integer id) {
        try {
            credencialService.delete(id);
            return ResponseEntity.ok("Credencial eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Permite iniciar sesión.
     * @param login credenciales de inicio de sesión
     * @return ResponseEntity con mensaje de confirmación o error y aumenta la cantidad de intentos fallidos
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión con sus credenciales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso."),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas.")
    })
    public ResponseEntity<String> login(@RequestBody @Parameter(description = "Credenciales de inicio de sesión", required = true)
                                        Login login) {
        boolean isAuthenticated = credencialService.verificarCredenciales(login.getCorreo(), login.getContrasenia());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    /**
     * Asigna un rol a una credencial.
     * @param credencialId ID de la credencial
     * @param rolId ID del rol a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{credencialId}/asignar-rol/{rolId}")
    @Operation(summary = "Asignar un rol a una credencial", description = "Asigna un rol a una credencial existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol asignado a la credencial exitosamente."),
            @ApiResponse(responseCode = "404", description = "Credencial o rol no encontrados."),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud.")
    })
    public ResponseEntity<String> asignarRol(@Parameter(description = "ID de la credencial", required = true)
                                             @PathVariable Integer credencialId,
                                             @Parameter(description = "ID del rol a asignar", required = true)
                                             @PathVariable Integer rolId) {
        try {
            credencialService.asignarRol(credencialId, rolId);
            return ResponseEntity.ok("Rol asignado a la credencial exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}