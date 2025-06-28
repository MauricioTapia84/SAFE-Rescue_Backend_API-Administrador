package com.SAFE_Rescue.API_Administrador.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un credencial en el sistema.
 * Contiene información sobre la composición y estado del credencial
 */
@Entity
@Table(name = "Rol")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Entidad que representa un rol")
public class Rol {

    /**
     * Identificador único del rol
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del rol", example = "1")
    private int id;

    /**
     * Nombre del rol
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Schema(description = "Nombre del rol", example = "Admin")
    @Column(length = 50, nullable = false)
    private String nombre;

}
