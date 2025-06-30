package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.RolRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
public class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockitoBean
    private RolRepository rolRepository;

    private Faker faker;
    private Rol rol;
    private Rol rolNulo;
    private Integer id;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        rol = new Rol(1, faker.job().position());
        rolNulo = new Rol(1, null);
        id = 1;
    }

    /**
     * Prueba para obtener todos los roles registrados en el sistema.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(rolRepository.findAll()).thenReturn(List.of(rol));

        // Act
        List<Rol> roles = rolService.findAll();

        // Assert
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(rol.getNombre(), roles.get(0).getNombre());
    }

    /**
     * Prueba para buscar un rol por su ID.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(rolRepository.findById(id)).thenReturn(Optional.of(rol));

        // Act
        Rol encontrado = rolService.findById(id);

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
        when(rolRepository.existsById(id)).thenReturn(true);
        doNothing().when(rolRepository).deleteById(id);

        // Act
        rolService.delete(id);

        // Assert
        verify(rolRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba para validar un rol en el sistema
    */
    @Test
    public void validarRolTest() {
        // Arrange
        String nombre = faker.job().position();
        if (nombre.length() > 50) {
            nombre = nombre.substring(0, 50);
        }

        Rol rol = new Rol(1, nombre);

        // Act & Assert
        assertDoesNotThrow(() -> rolService.validarRol(rol));
    }


    //ERRORES

    /**
     * Prueba para manejar la búsqueda de un rol por su ID de un rol que no existe.
     */
    @Test
    public void findByIdRolNoEncontradoTest() {
        // Arrange
        when(rolRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> rolService.findById(id));
    }


    /**
     * Prueba para validar los datos al guardar un rol.
     */
    @Test
    public void saveRolValidacionTest() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> rolService.save(rolNulo));
    }

    /**
     * Prueba para manejar la actualización de un rol que no existe.
     */
    @Test
    public void updateRolNoEncontradoTest() {
        // Arrange
        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> rolService.update(rol,id));
    }

    /**
     * Prueba para validar los datos al actualizar un rol.
     */
    @Test
    public void updateRolValidacionTest() {
        // Arrange
        when(rolRepository.findById(id)).thenReturn(Optional.of(new Rol(id,"NombreValido")));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> rolService.update(rolNulo,1));
    }


    /**
     * Prueba para manejar la eliminación de un rol que no existe.
     */
    @Test
    public void deleteRolNoEncontradoTest() {
        // Arrange
        when(rolRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> rolService.delete(id));
    }

    /**
     * Prueba para validar nombre nulo
     */
    @Test
    public void validarRolNombreNuloTest() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> rolService.validarRol(rolNulo));
    }

    /**
     * Prueba para validar nombre que excede el límite de caracteres
     */
    @Test
    public void validarRolNombreExcedeLimiteTest() {
        // Arrange
        Rol rolInvalido = new Rol(1,"EsteNombreEsDemasiadoLargoParaElLimiteDeCincuentaCaracteres");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> rolService.validarRol(rolInvalido));
    }
}