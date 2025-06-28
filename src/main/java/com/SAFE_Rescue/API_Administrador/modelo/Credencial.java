package com.SAFE_Rescue.API_Administrador.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una credencial en el sistema.
 * Contiene información sobre la composición y estado de la credencial.
 */
@Entity
@Table(name = "Credencial")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Credencial {

    /**
     * Identificador único de la credencial.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la credencial", example = "1")
    private int id;

    /**
     * Correo de la credencial.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 80 caracteres.
     */
    @Column(unique = true, length = 80, nullable = false)
    @Schema(description = "Correo de la credencial", example = "usuario@ejemplo.com", required = true)
    private String correo;

    /**
     * Contraseña de la credencial.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 16 caracteres.
     */
    @Column(length = 16, nullable = false)
    @Schema(description = "Contraseña de la credencial", example = "contraseña123", required = true)
    private String contrasenia;

    /**
     * Intentos fallidos del bombero al iniciar sesión.
     * Valor entero no negativo (>= 0).
     */
    @Column(length = 1, nullable = true)
    @Schema(description = "Número de intentos fallidos de inicio de sesión", example = "0")
    private int intentosFallidos = 0;

    /**
     * Estado de la credencial.
     * Debe ser un valor no nulo.
     */
    @Column(nullable = false)
    @Schema(description = "Estado de la credencial", example = "true", required = true)
    private boolean activo = true;

    /**
     * Rol asociado a la credencial.
     * Relación uno-a-muchos con la entidad Rol.
     */
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    @Schema(description = "Rol asociado a la credencial")
    private Rol rol;

}