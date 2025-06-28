package com.SAFE_Rescue.API_Administrador.modelo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entidad que representa un bombero en el sistema.
 * Contiene información sobre la composición y estado del bombero.
 */
@Entity
@Table(name = "Bombero")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bombero {

    /**
     * Identificador único del bombero.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del bombero", example = "1")
    private int id;

    /**
     * Run del Bombero.
     * Debe ser un valor no nulo, único y con una longitud máxima recomendada de 8 caracteres.
     */
    @Column(unique = true, length = 8, nullable = false)
    @Schema(description = "Run del bombero", example = "12345678", required = true)
    private int run;

    /**
     * Dígito verificador del Bombero.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 1 carácter.
     */
    @Column(length = 1, nullable = false)
    @Schema(description = "Dígito verificador del bombero", example = "K", required = true)
    private String dv;

    /**
     * Nombre descriptivo del bombero.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Nombre del bombero", example = "Juan", required = true)
    private String nombre;

    /**
     * Apellido paterno descriptivo del bombero.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(name = "a_paterno", length = 50, nullable = false)
    @Schema(description = "Apellido paterno del bombero", example = "Pérez", required = true)
    private String aPaterno;

    /**
     * Apellido materno descriptivo del bombero.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(name = "a_materno", length = 50, nullable = false)
    @Schema(description = "Apellido materno del bombero", example = "González", required = true)
    private String aMaterno;

    /**
     * Fecha de registro del Bombero.
     * Debe ser un valor no nulo.
     */
    @Column(name = "fecha_registro", nullable = false)
    @Schema(description = "Fecha de registro del bombero", example = "2022-01-01", required = true)
    private Date fechaRegistro;

    /**
     * Teléfono disponible del bombero.
     * Valor entero no negativo (>= 0).
     */
    @Column(unique = true, length = 9, nullable = false)
    @Schema(description = "Teléfono del bombero", example = "987654321", required = true)
    private int telefono;

    /**
     * Credenciales.
     * Relación uno-a-uno con la entidad Credencial.
     */
    @OneToOne
    @JoinColumn(name = "credenciales_id", referencedColumnName = "id")
    @Schema(description = "Credenciales asociadas al bombero")
    private Credencial credencial;
}