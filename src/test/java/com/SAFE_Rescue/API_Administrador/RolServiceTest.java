package com.SAFE_Rescue.API_Administrador;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.RolRepository;
import com.SAFE_Rescue.API_Administrador.service.RolService;
import net.datafaker.Faker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockitoBean
    private RolRepository rolRepository;

    @Test
    public void findAllRolesTest(){
        Faker faker = new Faker();

        when(rolRepository.findAll()).thenReturn(List.of(new Rol(1,(faker.job().position()))));


    }

}
