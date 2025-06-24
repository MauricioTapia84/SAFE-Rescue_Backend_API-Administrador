package com.SAFE_Rescue.API_Administrador.modelo;

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
     * Identificador único del bombero
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Run del Bombero
     * Debe ser un valor no nulo, único y con una longitud máxima recomendada de 8 caracteres
     */
    @Column(unique = true,length = 8,nullable = false)
    private Integer run;

    /**
     * Digito verificador del Bombero
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 1 caracteres
     */
    @Column(length = 1,nullable = false)
    private String dv;

    /**
     * Nombre descriptivo del bombero
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(length = 50,nullable = false)
    private String nombre;

    /**
     * Apellido paterno descriptivo del bombero
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(name="a_paterno",length = 50, nullable = false)
    private String aPaterno;

    /**
     * Apellido paterno descriptivo del bombero
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(name="a_materno",length = 50, nullable = false)
    private String aMaterno;

    /**
     * Fecha Registro del Bombero
     * Debe ser un valor no nulo
     */
    @Column(name= "fecha_registro",nullable = false)
    private Date fechaRegistro;

    /**
     * Telefono disponible del bombero
     * Valor entero no negativo (>= 0)
     * Representa unidades disponibles en inventario
     */
    @Column(unique = true, length = 9, nullable = false)
    private Integer telefono;

    /**
     * Credenciales
     * Relación uno-a-muchos
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credenciales_id", referencedColumnName = "id")
    private Credencial credencial;

}
