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

@SpringBootTest
public class CredencialServiceTest {

    @Autowired
    private CredencialService credencialService;

    @MockitoBean
    private CredencialRepository credencialRepository;

    @MockitoBean
    private RolRepository rolRepository;

    private Faker faker;
    private Set<String> uniqueCorreos;
    private Random random;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        uniqueCorreos = new HashSet<>();
        random = new Random();
    }

    @Test
    public void testFindAll() {
        List<Credencial> credenciales = List.of(new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, "Admin")));
        when(credencialRepository.findAll()).thenReturn(credenciales);

        List<Credencial> result = credencialService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Credencial credencial = new Credencial(id, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, "User"));
        when(credencialRepository.findById(id)).thenReturn(Optional.of(credencial));

        Credencial found = credencialService.findByID(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testFindById_CredencialNoExistente() {
        Integer id = 1;
        when(credencialRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> credencialService.findByID(id));
    }

    @Test
    public void testSave() {
        String correo = faker.internet().emailAddress();
        Credencial credencial = new Credencial(1, correo, faker.internet().password(), 0, true, new Rol(1, "Admin"));
        when(rolRepository.save(credencial.getRol())).thenReturn(credencial.getRol());
        when(credencialRepository.save(credencial)).thenReturn(credencial);

        Credencial saved = credencialService.save(credencial);

        assertNotNull(saved);
        assertEquals(correo, saved.getCorreo());
        verify(credencialRepository, times(1)).save(credencial);
    }

    @Test
    public void testSave_CredencialCorreoDuplicado() {
        Credencial credencial = new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, "User"));
        when(rolRepository.save(credencial.getRol())).thenReturn(credencial.getRol());
        when(credencialRepository.save(credencial)).thenThrow(new DataIntegrityViolationException(""));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> credencialService.save(credencial));
        assertEquals("El correo ya está en uso. Por favor, use otro.", exception.getMessage());
    }

    @Test
    public void testUpdate_CredencialExistente() {
        Integer id = 1;
        Credencial credencialExistente = new Credencial(id, faker.internet().emailAddress(), "oldPassword", 0, true, new Rol(1, "User"));
        when(credencialRepository.findById(id)).thenReturn(Optional.of(credencialExistente));

        Credencial credencialActualizada = new Credencial(id, null, "newPassword", 0, true, null);

        Credencial result = credencialService.update(credencialActualizada, id);

        assertNotNull(result);
        assertEquals("newPassword", result.getContrasenia());
        verify(credencialRepository, times(1)).save(credencialExistente);
    }

    @Test
    public void testUpdate_CredencialNoExistente() {
        Integer id = 1;
        when(credencialRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> credencialService.update(new Credencial(), id));
    }

    @Test
    public void testDelete_CredencialExistente() {
        Integer id = 1;
        when(credencialRepository.existsById(id)).thenReturn(true);

        credencialService.delete(id);

        verify(credencialRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDelete_CredencialNoExistente() {
        Integer id = 1;
        when(credencialRepository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> credencialService.delete(id));
    }
}