package com.SAFE_Rescue.API_Administrador.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un credencial en el sistema.
 * Contiene información sobre la composición y estado del credencial
 */
@Entity
@Table(name = "Credencial")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Credencial {

    /**
     * Identificador único de la credencial
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Correo de la credencial
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 80 caracteres
     */
    @Column(unique = true,length = 80,nullable = false)
    private String correo;

    /**
     * Contrasenia de la credencial
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 16 caracteres
     */
    @Column(length = 16, nullable = false)
    private String contrasenia;

    /**
     * Intentos Fallidos del Bombero al iniciar sesion
     * Valor entero no negativo (>= 0)
     */
    @Column(length = 1, nullable = true)
    private int intentosFallidos = 0;

    /**
     * Estadode la credencial
     * Debe ser un valor no nulo
     */
    @Column(nullable = false)
    private boolean activo = true;

    /**
     * Rol
     * Relación uno-a-muchos
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Rol rol;

}
