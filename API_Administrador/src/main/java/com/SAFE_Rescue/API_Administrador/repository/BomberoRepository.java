package com.SAFE_Rescue.API_Administrador.repository;

import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomberoRepository extends JpaRepository<Bombero , Long> {

    Bombero findByRun(int Run);

    List<Bombero> findByAPaterno(String APaterno);

    List<Bombero> findByCorreo(String Correo);

    List<Bombero> findByNombreCompleto(String Nombre,String APaterno,String AMaterno);

    List<Bombero> findByNombre(String Nombre);



}
