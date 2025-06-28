package com.SAFE_Rescue.API_Administrador.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad DTO que representa un Login en el sistema.
 * Contiene información sobre la composición y estado del Login.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Login {

    /**
     * Correo de la credencial.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 80 caracteres.
     */
    @Schema(description = "Correo de la credencial", example = "usuario@ejemplo.com", required = true)
    private String correo;

    /**
     * Contraseña de la credencial.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 16 caracteres.
     */
    @Schema(description = "Contraseña de la credencial", example = "contraseña123", required = true)
    private String contrasenia;
}
