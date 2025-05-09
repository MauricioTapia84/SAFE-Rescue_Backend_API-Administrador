package com.SAFE_Rescue.API_Administrador.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Bombero")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bombero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "run_bom", unique = true,length = 8,nullable = false)
    private int run;

    @Column(name = "dv_bom", unique = true,length = 1,nullable = false)
    private String dv;

    @Column(name = "nombre_bom",length = 50,nullable = false)
    private String nombre;

    @Column(name = "apaterno_bom",length = 50,nullable = false)
    private String aPaterno;

    @Column(name = "amaterno_bom",length = 50,nullable = false)
    private String aMaterno;

    @Column(name = "fecha_registro_bom",nullable = false)
    private Date fechaReistro;

    @Column(name = "correo_bom",unique = true,length = 80,nullable = false)
    private String correo;

    @Column(name = "fono_bom",unique = true,length = 9,nullable = false)
    private int telefono;

    @Column(name = "pass_bom",length = 10,nullable = false)
    private String contrasenia;


}
