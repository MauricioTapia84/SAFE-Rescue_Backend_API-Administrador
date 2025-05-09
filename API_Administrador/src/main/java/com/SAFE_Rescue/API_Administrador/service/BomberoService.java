package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.repository.BomberoRepository;
import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class BomberoService {

    @Autowired
    private BomberoRepository bomberoRepository;

    public List<Bombero> findAll(){
        return bomberoRepository.findAll();
    }

    public Bombero findByID(Long id){
        return bomberoRepository.findById(id).get();
    }

    @NotNull
    public Bombero findByRun(String run){

        int rut;
        String runSinPuntos= run.replace(".","");
        String runSinDV= runSinPuntos.substring(0, runSinPuntos.length() - 2);

        try{
            rut = Integer.parseInt(runSinDV);
            return bomberoRepository.findByRun(rut);
        }catch (NumberFormatException e){
            System.out.println("Error: El string no es un número válido.");
            return null;
        }

    }

    //save funciona para crear nuevas tablas y para actualizar
    public Bombero save(Bombero bombero){
        return bomberoRepository.save(bombero);
    }

    public void delete(Long id){
        bomberoRepository.deleteById(id);
    }


}
