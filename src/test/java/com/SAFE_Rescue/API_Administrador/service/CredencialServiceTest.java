package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.CredencialRepository;
import com.SAFE_Rescue.API_Administrador.repository.RolRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para el servicio CredencialService.
 * Esta clase verifica la funcionalidad de los métodos dentro de CredencialService,
 * incluyendo operaciones CRUD y validaciones.
 */
@SpringBootTest
public class CredencialServiceTest {

    @Autowired
    private CredencialService credencialService;

    @MockitoBean
    private CredencialRepository credencialRepository;

    @MockitoBean
    private RolService rolService;

    @MockitoBean
    private RolRepository rolRepository;

    private Faker faker;
    private Credencial credencial;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker, un conjunto para correos únicos y un generador de números aleatorios.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        credencial = new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, faker.job().position()));
        id = 1;

    }

    /**
     * Prueba que verifica la obtención de todas las credenciales.
     * Asegura que el servicio devuelve la lista correcta de credenciales.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(credencialRepository.findAll()).thenReturn(List.of(credencial));

        // Act
        List<Credencial> credenciales = credencialService.findAll();

        // Assert
        assertNotNull(credenciales);
        assertEquals(1, credenciales.size());
        assertEquals(credencial.getCorreo(), credenciales.get(0).getCorreo());
        assertEquals(credencial.getContrasenia(), credenciales.get(0).getContrasenia());
        assertEquals(credencial.getIntentosFallidos(), credenciales.get(0).getIntentosFallidos());
        assertEquals(credencial.isActivo(), credenciales.get(0).isActivo());
        assertEquals(credencial.getRol().getNombre(), credenciales.get(0).getRol().getNombre());
    }

    /**
     * Prueba que verifica la búsqueda de una credencial por su ID.
     * Asegura que se encuentra la credencial correcta.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(credencialRepository.findById(id)).thenReturn(Optional.of(credencial));

        // Act
        Credencial encontrada = credencialService.findByID(id);

        // Assert
        assertNotNull(encontrada);
        assertEquals(id, encontrada.getId());
        assertEquals(credencial.getCorreo(), encontrada.getCorreo());
        assertEquals(credencial.getContrasenia(), encontrada.getContrasenia());
        assertEquals(credencial.getIntentosFallidos(), encontrada.getIntentosFallidos());
        assertEquals(credencial.isActivo(), encontrada.isActivo());
        assertEquals(credencial.getRol().getNombre(), encontrada.getRol().getNombre());
    }

    /**
     * Prueba que verifica la creación y guardado de una nueva credencial.
     * Asegura que la credencial se guarda correctamente en el repositorio.
     */
    @Test
    public void saveTest() {
        // Arrange
        when(rolService.save(credencial.getRol())).thenReturn(credencial.getRol());
        when(credencialRepository.save(credencial)).thenReturn(credencial);

        // Act
        Credencial guardado = credencialService.save(credencial);

        // Assert
        assertNotNull(guardado);
        assertEquals(credencial.getCorreo(), guardado.getCorreo());
        assertEquals(credencial.getContrasenia(), guardado.getContrasenia());
        assertEquals(credencial.getIntentosFallidos(), guardado.getIntentosFallidos());
        assertEquals(credencial.isActivo(), guardado.isActivo());
        assertEquals(credencial.getRol().getId(), guardado.getRol().getId());
        assertEquals(credencial.getRol().getNombre(), guardado.getRol().getNombre());
        verify(credencialRepository, times(1)).save(credencial);
    }

    /**
     * Prueba que verifica la actualización de una credencial existente.
     * Asegura que la credencial se actualiza correctamente.
     */
    @Test
    public void udpateTest() {
        // Arrange
        Credencial credencialExistente = new Credencial(id, faker.internet().emailAddress(), "oldPassword", 0, true, new Rol(1, "Usuario"));
        Credencial credencialActualizada = new Credencial(id, faker.job().position(), "newPassword", 0, true, new Rol(2, "Administrador"));
        when(credencialRepository.findById(id)).thenReturn(Optional.of(credencialExistente));
        when(credencialRepository.save(credencialExistente)).thenReturn(credencialActualizada);

        // Act
        Credencial actualizada = credencialService.update(credencialActualizada, id);

        // Assert
        assertNotNull(actualizada);
        assertEquals("newPassword", actualizada.getContrasenia());
        assertEquals(credencialActualizada.getCorreo(), actualizada.getCorreo());
        assertEquals(credencialActualizada.getContrasenia(), actualizada.getContrasenia());
        assertEquals(credencialActualizada.isActivo(), actualizada.isActivo());
        assertEquals(credencialActualizada.getRol().getId(), actualizada.getRol().getId());
        assertEquals(credencialActualizada.getRol().getNombre(), actualizada.getRol().getNombre());
        verify(credencialRepository, times(1)).save(credencialExistente);
    }

    /**
     * Prueba que verifica la eliminación de una credencial.
     * Asegura que la credencial se elimina correctamente del repositorio.
     */
    @Test
    public void deleteTest() {
        // Arrange
        when(credencialRepository.existsById(id)).thenReturn(true);
        doNothing().when(credencialRepository).deleteById(id);

        // Act
        credencialService.delete(id);

        // Assert
        verify(credencialRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba que verifica la validación de una credencial.
     * Asegura que una credencial válida no lanza excepciones.
     */
    @Test
    public void validarCredencialTest() {
        // Act & Assert
        assertDoesNotThrow(() -> credencialService.validarCredencial(credencial));
    }

    /**
     * Prueba que verifica la verificación de credenciales al iniciar sesión.
     * Asegura que las credenciales correctas devuelven true y no incrementan intentos fallidos.
     */
    @Test
    public void verificarCredencialesTest() {
        // Arrange
        Credencial credencialV = new Credencial();
        credencialV.setContrasenia("ContraseniaValida");
        credencialV.setIntentosFallidos(0);
        when(credencialRepository.findByCorreo("correo@ejemplo.com")).thenReturn(credencialV);

        // Act
        boolean resultado = credencialService.verificarCredenciales("correo@ejemplo.com", "ContraseniaValida");

        // Assert
        assertTrue(resultado);
        assertEquals(0, credencialV.getIntentosFallidos());
    }

    /**
     * Prueba que verifica la asignación de un rol a una credencial.
     * Asegura que se asigna correctamente y no lanza excepciones.
     */
    @Test
    public void asignarRolTest() {
        // Arrange
        Rol rol = new Rol(1, faker.job().position());
        Credencial credencial = new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, null);

        when(credencialRepository.findById(1)).thenReturn(Optional.of(credencial));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        // Act
        assertDoesNotThrow(() -> credencialService.asignarRol(1, 1));

        // Assert
        assertEquals(rol, credencial.getRol());
        verify(credencialRepository).save(credencial);
    }

    // ERRORES

    /**
     * Prueba que verifica la búsqueda por ID cuando la credencial no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void findByIdTest_CredencialNoExiste() {
        // Arrange
        when(credencialRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () ->  credencialService.findByID(id));
    }

    /**
     * Prueba que verifica el manejo de un correo duplicado al guardar una credencial.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void saveTest_CorreoDuplicado() {
        // Arrange
        String correoDuplicado = "correo@ejemplo.com";
        Credencial credencialT = new Credencial(1, correoDuplicado, faker.internet().password(), 0, true, new Rol(1, faker.job().position()));

        when(rolService.save(credencialT.getRol())).thenReturn(credencialT.getRol());
        when(credencialRepository.save(credencialT)).thenThrow(new DataIntegrityViolationException(""));

        // Assert
        assertThrows(RuntimeException.class, () -> credencialService.save(credencialT));
    }


    /**
     * Prueba que verifica el intento de actualización de una credencial que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void testUpdate_CredencialNoExistente() {
        // Arrange
        when(credencialRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> credencialService.update(new Credencial(), id));
    }

    /**
     * Prueba que verifica la eliminación de una credencial que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void testDelete_CredencialNoExistente() {
        // Arrange
        when(credencialRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> credencialService.delete(id));
    }

    /**
     * Prueba que verifica la validación de la credencial cuando la contraseña es nula.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void validarCredencialContraseniaNula() {
        Credencial credencialV = new Credencial();
        credencialV.setContrasenia(null);
        assertThrows(IllegalArgumentException.class, () -> credencialService.validarCredencial(credencialV));
    }

    /**
     * Prueba que verifica la validación de la credencial cuando la contraseña excede el límite.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void validarCredencialContraseniaExcedeLimite() {
        Credencial credencialV = new Credencial();
        credencialV.setContrasenia("EstaContraseniaEsDemasiadoLarga");
        assertThrows(RuntimeException.class, () -> credencialService.validarCredencial(credencialV));
    }

    /**
     * Prueba que verifica la validación de la credencial cuando el correo es nulo.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void validarCredencialCorreoNulo() {
        Credencial credencialV = new Credencial();
        credencialV.setCorreo(null);
        assertThrows(IllegalArgumentException.class, () -> credencialService.validarCredencial(credencialV));
    }

    /**
     * Prueba que verifica la validación de la credencial cuando el correo excede el límite.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void validarCredencialCorreoExcedeLimite() {
        Credencial credencialV = new Credencial();
        credencialV.setCorreo("correo@ejemplo.com".repeat(6)); // Más de 80 caracteres
        assertThrows(RuntimeException.class, () -> credencialService.validarCredencial(credencialV));
    }

    /**
     * Prueba que verifica la validación de la credencial cuando los intentos fallidos son negativos.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void validarCredencialIntentosFallidosNegativos() {
        Credencial credencialV = new Credencial();
        credencialV.setIntentosFallidos(-1);
        assertThrows(IllegalArgumentException.class, () -> credencialService.validarCredencial(credencialV));
    }

    /**
     * Prueba que verifica la verificación de credenciales fallidas.
     * Asegura que se incrementan los intentos fallidos correctamente.
     */
    @Test
    public void verificarCredencialesFallidas() {
        Credencial credencialV = new Credencial();
        credencialV.setContrasenia("ContraseniaValida");
        credencialV.setIntentosFallidos(0);

        when(credencialRepository.findByCorreo("correo@ejemplo.com")).thenReturn(credencialV);

        boolean resultado = credencialService.verificarCredenciales("correo@ejemplo.com", "ContraseniaIncorrecta");

        assertFalse(resultado);
        assertEquals(1, credencialV.getIntentosFallidos());
    }

    /**
     * Prueba que verifica la verificación de credenciales cuando el correo no se encuentra.
     * Asegura que el resultado es falso.
     */
    @Test
    public void verificarCredencialesCorreoNoEncontrado() {
        when(credencialRepository.findByCorreo("correo@ejemplo.com")).thenReturn(null);

        boolean resultado = credencialService.verificarCredenciales("correo@ejemplo.com", "ContraseniaValida");

        assertFalse(resultado);
    }

    /**
     * Prueba que verifica el manejo de un rol no encontrado al asignar un rol.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void asignarRolRolNoEncontrado() {
        when(rolRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> credencialService.asignarRol(1, 1));
    }

    /**
     * Prueba que verifica el manejo de una credencial no encontrada al asignar un rol.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void asignarRolCredencialNoEncontrada() {
        Rol rol = new Rol(1, "RolValido");

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(credencialRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> credencialService.asignarRol(1, 1));
    }
}