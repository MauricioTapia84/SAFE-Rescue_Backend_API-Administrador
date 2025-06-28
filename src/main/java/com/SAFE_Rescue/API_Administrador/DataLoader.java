package com.SAFE_Rescue.API_Administrador;

import com.SAFE_Rescue.API_Administrador.repository.*;
import com.SAFE_Rescue.API_Administrador.modelo.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
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
    public void run(String... args) throws Exception {
        System.out.println("DataLoader is running...");

        Faker faker = new Faker();
        Random random = new Random();
        Set<Integer> uniqueRuts = new HashSet<>();
        Set<Integer> uniqueTelefonos = new HashSet<>();
        Set<String> uniqueCorreos = new HashSet<>();
        Set<Credencial> uniqueCredencial = new HashSet<>();

        // Generar Roles
        for (int i = 0; i < 3; i++) {
            Rol rol = new Rol();
            rol.setNombre(faker.job().position());
            try {
                rolRepository.save(rol);
            } catch (Exception e) {
                System.out.println("Error al guardar rol: " + e.getMessage());
            }
        }

        List<Rol> roles = rolRepository.findAll();
        if (roles.isEmpty()) {
            System.out.println("No se encontraron roles, agregue roles primero");
            return; // Detén la ejecución si no hay roles
        }

        // Generar Credenciales
        for (int i = 0; i < 5; i++) {
            Credencial credencial = new Credencial();
            String correo;
            do {
                correo = faker.internet().emailAddress();
            } while (uniqueCorreos.contains(correo));
            uniqueCorreos.add(correo);
            credencial.setCorreo(correo);
            credencial.setContrasenia(faker.internet().password());
            credencial.setIntentosFallidos(faker.number().numberBetween(0, 9));
            credencial.setActivo(faker.random().nextBoolean());
            Rol rol = roles.get(random.nextInt(roles.size()));
            credencial.setRol(rol);
            try {
                credencialRepository.save(credencial);
            } catch (Exception e) {
                System.out.println("Error al guardar credencial: " + e.getMessage());
            }
        }

        List<Credencial> credenciales = credencialRepository.findAll();
        if (credenciales.isEmpty()) {
            System.out.println("No se encontraron Credenciales, agregue Credenciales primero");
            return; // Detén la ejecución si no hay Credenciales
        }

        // Generar Bomberos
        for (int i = 0; i < 10; i++) {
            Bombero bombero = new Bombero();
            Credencial credencialAsignada;
            int rut;
            int telefono;

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

            do {
                telefono = faker.number().numberBetween(100000000, 999999999);
            } while (uniqueTelefonos.contains(telefono));

            uniqueTelefonos.add(telefono);
            bombero.setTelefono(telefono);
            do {
                credencialAsignada = credenciales.get(random.nextInt(credenciales.size()));
            } while (uniqueCredencial.contains(credencialAsignada));

            uniqueCredencial.add(credencialAsignada);
            bombero.setCredencial(credencialAsignada);
            try {
                bomberoRepository.save(bombero);
            } catch (Exception e) {
                System.out.println("Error al guardar Bombero: " + e.getMessage());
            }
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