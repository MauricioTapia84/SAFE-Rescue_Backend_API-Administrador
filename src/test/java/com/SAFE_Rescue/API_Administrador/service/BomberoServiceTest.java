package com.SAFE_Rescue.API_Administrador.service;

import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.repository.BomberoRepository;
import com.SAFE_Rescue.API_Administrador.repository.CredencialRepository;
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

@SpringBootTest
public class BomberoServiceTest {

    @Autowired
    private BomberoService bomberoService;

    @MockitoBean
    private BomberoRepository bomberoRepository;

    @MockitoBean
    private CredencialRepository credencialRepository;

    @MockitoBean
    private CredencialService credencialService;

    private Faker faker;
    private int rut;
    private Bombero bombero;
    private Integer id;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        id = 1;
        rut = faker.number().numberBetween(1000000, 99999999);
        bombero = new Bombero(1,rut,calcularDv(rut),faker.name().firstName(),faker.name().lastName(),faker.name().lastName(),new Date(),faker.number().numberBetween(100000000, 999999999),new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, faker.job().position())));
    }

    @Test
    public void findAllTest() {
        // Arrange
        when(bomberoRepository.findAll()).thenReturn(List.of(bombero));

        // Act
        var bomberos = bomberoService.findAll();

        // Assert
        assertNotNull(bomberos);
        assertEquals(1, bomberos.size());
        assertEquals(bombero.getRun(), bomberos.get(0).getRun());
        assertEquals(bombero.getDv(), bomberos.get(0).getDv());
        assertEquals(bombero.getNombre(), bomberos.get(0).getNombre());
        assertEquals(bombero.getAPaterno(), bomberos.get(0).getAPaterno());
        assertEquals(bombero.getAMaterno(), bomberos.get(0).getAMaterno());
        assertEquals(bombero.getFechaRegistro(), bomberos.get(0).getFechaRegistro());
        assertEquals(bombero.getTelefono(), bomberos.get(0).getTelefono());
        assertEquals(bombero.getCredencial().getCorreo(), bomberos.get(0).getCredencial().getCorreo());
        assertEquals(bombero.getCredencial().getContrasenia(), bomberos.get(0).getCredencial().getContrasenia());
        assertEquals(bombero.getCredencial().getIntentosFallidos(), bomberos.get(0).getCredencial().getIntentosFallidos());
        assertEquals(bombero.getCredencial().isActivo(), bomberos.get(0).getCredencial().isActivo());
        assertEquals(bombero.getCredencial().getRol().getNombre(), bomberos.get(0).getCredencial().getRol().getNombre());
    }

    @Test
    public void findByIDTest() {
        // Arrange
        when(bomberoRepository.findById(id)).thenReturn(Optional.of(bombero));

        // Act
        Bombero encontrado = bomberoService.findByID(id);

        // Assert
        assertNotNull(encontrado);
        assertEquals(bombero.getRun(),encontrado.getRun());
        assertEquals(bombero.getDv(),encontrado.getDv());
        assertEquals(bombero.getNombre(),encontrado.getNombre());
        assertEquals(bombero.getAPaterno(),encontrado.getAPaterno());
        assertEquals(bombero.getAMaterno(),encontrado.getAMaterno());
        assertEquals(bombero.getFechaRegistro(),encontrado.getFechaRegistro());
        assertEquals(bombero.getTelefono(),encontrado.getTelefono());
        assertEquals(bombero.getCredencial().getCorreo(),encontrado.getCredencial().getCorreo());
        assertEquals(bombero.getCredencial().getContrasenia(),encontrado.getCredencial().getContrasenia());
        assertEquals(bombero.getCredencial().getIntentosFallidos(),encontrado.getCredencial().getIntentosFallidos());
        assertEquals(bombero.getCredencial().isActivo(),encontrado.getCredencial().isActivo());
        assertEquals(bombero.getCredencial().getRol().getNombre(),encontrado.getCredencial().getRol().getNombre());
    }

    @Test
    public void saveTest() {
        // Arrange
        when(credencialService.save(bombero.getCredencial())).thenReturn(bombero.getCredencial());
        when(bomberoRepository.save(bombero)).thenReturn(bombero);

        // Act
        Bombero guardado = bomberoService.save(bombero);

        // Assert
        assertNotNull(guardado);
        assertEquals(bombero.getRun(),guardado.getRun());
        assertEquals(bombero.getDv(),guardado.getDv());
        assertEquals(bombero.getNombre(),guardado.getNombre());
        assertEquals(bombero.getAPaterno(),guardado.getAPaterno());
        assertEquals(bombero.getAMaterno(),guardado.getAMaterno());
        assertEquals(bombero.getFechaRegistro(),guardado.getFechaRegistro());
        assertEquals(bombero.getTelefono(),guardado.getTelefono());
        assertEquals(bombero.getCredencial().getCorreo(),guardado.getCredencial().getCorreo());
        assertEquals(bombero.getCredencial().getContrasenia(),guardado.getCredencial().getContrasenia());
        assertEquals(bombero.getCredencial().getIntentosFallidos(),guardado.getCredencial().getIntentosFallidos());
        assertEquals(bombero.getCredencial().isActivo(),guardado.getCredencial().isActivo());
        assertEquals(bombero.getCredencial().getRol().getNombre(),guardado.getCredencial().getRol().getNombre());
        verify(bomberoRepository, times(1)).save(bombero);
    }


    @Test
    public void updateTest() {
        // Arrange
        int rutExistente = faker.number().numberBetween(1000000, 99999999);
        int rutActualizado = faker.number().numberBetween(1000000, 99999999);
        Bombero bomberoExistente = new Bombero(1,rutExistente,calcularDv(rutExistente),faker.name().firstName(),faker.name().lastName(),faker.name().lastName(),new Date(),faker.number().numberBetween(100000000, 999999999),new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, "Admin")));
        Bombero bomberoActualizado = new Bombero(1,rutActualizado,calcularDv(rutActualizado),faker.name().firstName(),faker.name().lastName(),faker.name().lastName(),new Date(),faker.number().numberBetween(100000000, 999999999),new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, "Admin")));
        when(bomberoRepository.findById(id)).thenReturn(Optional.of(bomberoExistente));
        when(bomberoRepository.save(bomberoExistente)).thenReturn(bomberoActualizado);

        // Act
        Bombero actualizado = bomberoService.update(bomberoActualizado, id);

        // Assert
        assertNotNull(actualizado);
        assertEquals(bomberoActualizado.getRun(),actualizado.getRun());
        assertEquals(bomberoActualizado.getDv(),actualizado.getDv());
        assertEquals(bomberoActualizado.getNombre(),actualizado.getNombre());
        assertEquals(bomberoActualizado.getAPaterno(),actualizado.getAPaterno());
        assertEquals(bomberoActualizado.getAMaterno(),actualizado.getAMaterno());
        assertEquals(bomberoActualizado.getFechaRegistro(),actualizado.getFechaRegistro());
        assertEquals(bomberoActualizado.getTelefono(),actualizado.getTelefono());
        assertEquals(bomberoActualizado.getCredencial().getCorreo(),actualizado.getCredencial().getCorreo());
        assertEquals(bomberoActualizado.getCredencial().getContrasenia(),actualizado.getCredencial().getContrasenia());
        assertEquals(bomberoActualizado.getCredencial().getIntentosFallidos(),actualizado.getCredencial().getIntentosFallidos());
        assertEquals(bomberoActualizado.getCredencial().isActivo(),actualizado.getCredencial().isActivo());
        assertEquals(bomberoActualizado.getCredencial().getRol().getNombre(),actualizado.getCredencial().getRol().getNombre());
        verify(bomberoRepository, times(1)).save(bomberoExistente);
    }

    @Test
    public void deleteTest() {
        // Arrange
        when(bomberoRepository.existsById(id)).thenReturn(true);

        // Act
        bomberoService.delete(id);

        // Assert
        verify(bomberoRepository, times(1)).deleteById(id);
    }


    @Test
    public void asignarCredencialTest() {
        // Arrange
        Integer bomberoId = 1;
        Integer credencialId = 1;
        Bombero bomberoA = new Bombero();
        Credencial credencialA = new Credencial();
        when(bomberoRepository.findById(bomberoId)).thenReturn(Optional.of(bomberoA));
        when(credencialRepository.findById(credencialId)).thenReturn(Optional.of(credencialA));

        // Act
        assertDoesNotThrow(() -> bomberoService.asignarCredencial(bomberoId, credencialId));

        // Assert
        assertEquals(credencialA, bomberoA.getCredencial());
        verify(bomberoRepository, times(1)).save(bomberoA);
    }


    //ERRORES

    @Test
    public void findByIDTest_BomberoNoExistente() {
        // Arrange
        when(bomberoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> bomberoService.findByID(id));
    }

    @Test
    public void saveTest_ErrorCorreoDuplicado() {
        // Arrange
        Bombero bomberoS = new Bombero();
        Credencial credencialS = new Credencial();
        bomberoS.setCredencial(credencialS);
        when(credencialService.save(credencialS)).thenThrow(new DataIntegrityViolationException(""));

        // Assert
        assertThrows(RuntimeException.class, () -> bomberoService.save(bomberoS));
    }

    @Test
    public void updateTest_BomberoNoExistente() {
        // Arrange
        when(bomberoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bomberoService.update(new Bombero(), id);
        });
    }

    @Test
    public void deleteTest_BomberoNoExistente() {
        // Arrange
        when(bomberoRepository.existsById(id)).thenReturn(false);

        // Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bomberoService.delete(id);
        });
        assertEquals("Bombero no encontrado", exception.getMessage());
    }

    @Test
    public void validarBomberoTest_NombreNulo() {
        Bombero bomberoV = new Bombero();
        bomberoV.setNombre(null);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bomberoService.validarBombero(bomberoV);
        });
    }

    @Test
    public void asignarCredencialTest_BomberoNoEncontrado() {
        // Arrange
        Integer bomberoId = 1;
        Integer credencialId = 1;
        when(bomberoRepository.findById(bomberoId)).thenReturn(Optional.empty());

        // Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bomberoService.asignarCredencial(bomberoId, credencialId);
        });
        assertEquals("Bombero no encontrado", exception.getMessage());
    }

    @Test
    public void asignarCredencialTest_CredencialNoEncontrada() {
        // Arrange
        Integer bomberoId = 1;
        Integer credencialId = 1;
        Bombero bomberoA = new Bombero();
        when(bomberoRepository.findById(bomberoId)).thenReturn(Optional.of(bomberoA));
        when(credencialRepository.findById(credencialId)).thenReturn(Optional.empty());

        // Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bomberoService.asignarCredencial(bomberoId, credencialId);
        });
        assertEquals("Credencial no encontrada", exception.getMessage());
    }


    // Calcular Dígito verificador

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