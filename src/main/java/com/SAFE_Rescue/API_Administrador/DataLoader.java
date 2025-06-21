package com.SAFE_Rescue.API_Administrador;

import com.SAFE_Rescue.API_Administrador.repository.*;
import com.SAFE_Rescue.API_Administrador.modelo.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    BomberoRepository bomberoRepository;
    @Autowired
    CredencialRepository credencialRepository;
    @Autowired
    RolRepository rolRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        Set<Integer> uniqueRuts = new HashSet<>();

        // Generar Roles
        for (int i = 0; i < 3; i++) {
            Rol rol = new Rol();
            rol.setNombre(faker.job().position());
            rolRepository.save(rol);
        }

        List<Rol> roles = rolRepository.findAll();

        // Generar Credenciales
        for (int i = 0; i < 5; i++) {
            Credencial credencial = new Credencial();
            credencial.setCorreo(faker.internet().emailAddress());
            credencial.setIntentosFallidos(faker.number().numberBetween(0, 9));
            credencial.setActivo(faker.random().nextBoolean());
            credencial.setRol(roles.get(random.nextInt(roles.size())));
            credencialRepository.save(credencial);
        }

        List<Credencial> credenciales = credencialRepository.findAll();

        // Generar Bomberos
        for (int i = 0; i < 10; i++) {
            Bombero bombero = new Bombero();
            int rut;
            do {
                rut = faker.number().numberBetween(1000000, 99999999);
            } while (uniqueRuts.contains(rut));
            uniqueRuts.add(rut);
            bombero.setRun(rut);
            bombero.setDv(calcularDv(rut));
            bombero.setNombre(faker.name().firstName());
            bombero.setAPaterno(faker.name().lastName());
            bombero.setAMaterno(faker.name().lastName());
            bombero.setFechaRegistro(new Date());
            bombero.setTelefono(faker.number().numberBetween(100000000, 999999999));
            bombero.setCredencial(credenciales.get(random.nextInt(credenciales.size())));
            bomberoRepository.save(bombero);
        }
    }

    public String calcularDv(int rut) {
        int suma = 0;
        int multiplicador = 2;

        while (rut > 0) {
            suma += (rut % 10) * multiplicador;
            rut /= 10;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int dv = 11 - (suma % 11);
        if (dv == 11) return "0";
        if (dv == 10) return "K";
        return String.valueOf(dv);
    }
}