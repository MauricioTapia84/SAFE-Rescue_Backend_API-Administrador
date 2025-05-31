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
@Table(name = "Rol")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rol {

    /**
     * Identificador único del rol
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * nombre del rol
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(length = 50, nullable = false)
    private String nombre;

}
