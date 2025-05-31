package com.SAFE_Rescue.API_Administrador.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un Login en el sistema.
 * Contiene información sobre la composición y estado del Login
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Login {

    /**
     * Correo de la credencial
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 80 caracteres
     */
    private String correo;

    /**
     * Contrasenia de la credencial
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 16 caracteres
     */
    private String contrasenia;

}
