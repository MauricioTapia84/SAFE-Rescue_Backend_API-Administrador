package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.RolRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockitoBean
    private RolRepository rolRepository;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    /**
     * Prueba para obtener todos los roles registrados en el sistema.
     */
    @Test
    public void findAllRolesTest() {
        // Arrange
        Rol rol = new Rol(1, faker.job().position());
        when(rolRepository.findAll()).thenReturn(List.of(rol));

        // Act
        List<Rol> roles = rolService.findAllRoles();

        // Assert
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(rol.getNombre(), roles.get(0).getNombre());
    }

    /**
     * Prueba para buscar un rol por su ID.
     */
    @Test
    public void findByRolTest() {
        // Arrange
        Integer id = 1;
        Rol rol = new Rol(id, faker.job().position());
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));

        // Act
        Rol encontrado = rolService.findByRol(id);

        // Assert
        assertNotNull(encontrado);
        assertEquals(id, encontrado.getId());
        assertEquals(rol.getNombre(), encontrado.getNombre());
    }

    /**
     * Prueba para guardar un nuevo rol en el sistema.
     */
    @Test
    public void saveTest() {
        // Arrange
        Rol rol = new Rol(1, faker.job().position());
        when(rolRepository.save(rol)).thenReturn(rol);

        // Act
        Rol guardado = rolService.save(rol);

        // Assert
        assertNotNull(guardado);
        assertEquals(rol.getNombre(), guardado.getNombre());
        verify(rolRepository, times(1)).save(rol);
    }

    /**
     * Prueba para actualizar un rol existente.
     */
    @Test
    public void updateTest() {
        // Arrange
        Integer id = 1;
        Rol rolExistente = new Rol(id, faker.job().position());
        Rol rolActualizado = new Rol(id, faker.job().title());
        when(rolRepository.findById(id)).thenReturn(Optional.of(rolExistente));
        when(rolRepository.save(rolExistente)).thenReturn(rolActualizado);

        // Act
        Rol actualizado = rolService.update(rolActualizado, id);

        // Assert
        assertNotNull(actualizado);
        assertEquals(rolActualizado.getNombre(), actualizado.getNombre());
        assertEquals(id, actualizado.getId());
        verify(rolRepository, times(1)).save(rolExistente);
    }

    /**
     * Prueba para eliminar un rol del sistema.
     */
    @Test
    public void deleteTest() {
        // Arrange
        Integer id = 1;
        when(rolRepository.existsById(id)).thenReturn(true);
        doNothing().when(rolRepository).deleteById(id);

        // Act
        rolService.delete(id);

        // Assert
        verify(rolRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba para manejar la eliminación de un rol que no existe.
     */
    @Test
    public void deleteRolNoEncontradoTest() {
        // Arrange
        Integer id = 1;
        when(rolRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> rolService.delete(id));
    }

    /**
     * Prueba para validar los datos al guardar un rol.
     */
    @Test
    public void saveRolValidationTest() {
        // Arrange
        Rol rolInvalido = new Rol(1, null); // Nombre nulo

        // Assert
        assertThrows(IllegalArgumentException.class, () -> rolService.save(rolInvalido));
    }
}