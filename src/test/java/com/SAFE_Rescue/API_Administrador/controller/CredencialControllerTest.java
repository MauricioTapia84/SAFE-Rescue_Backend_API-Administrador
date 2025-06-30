package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.modelo.Login;
import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.service.CredencialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de prueba para el controlador CredencialController.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento
 * de los endpoints relacionados con las credenciales.
 */
@WebMvcTest(CredencialController.class)
public class CredencialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CredencialService credencialService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private Credencial credencial;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto Credencial para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        credencial = new Credencial(1, faker.internet().emailAddress(), faker.internet().password(), 0, true, new Rol(1, faker.job().position()));
        id = 1;
    }

    /**
     * Prueba que verifica la obtención de todas las credenciales existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de credenciales.
     */
    @Test
    public void listarTest() throws Exception {
        // Arrange
        when(credencialService.findAll()).thenReturn(List.of(credencial));

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/credenciales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(credencial.getId()))
                .andExpect(jsonPath("$[0].correo").value(credencial.getCorreo()))
                .andExpect(jsonPath("$[0].contrasenia").value(credencial.getContrasenia()))
                .andExpect(jsonPath("$[0].intentosFallidos").value(credencial.getIntentosFallidos()))
                .andExpect(jsonPath("$[0].activo").value(credencial.isActivo()))
                .andExpect(jsonPath("$[0].rol").value(credencial.getRol()));
    }

    /**
     * Prueba que verifica la búsqueda de una credencial existente por su ID.
     * Asegura que se devuelve un estado 200 OK y la credencial encontrada.
     */
    @Test
    public void buscarCredencialTest() throws Exception {
        // Arrange
        when(credencialService.findByID(id)).thenReturn(credencial);

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/credenciales/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(credencial.getId()))
                .andExpect(jsonPath("$.correo").value(credencial.getCorreo()))
                .andExpect(jsonPath("$.contrasenia").value(credencial.getContrasenia()))
                .andExpect(jsonPath("$.intentosFallidos").value(credencial.getIntentosFallidos()))
                .andExpect(jsonPath("$.activo").value(credencial.isActivo()))
                .andExpect(jsonPath("$.rol").value(credencial.getRol()));
    }

    /**
     * Prueba que verifica la creación de una nueva credencial.
     * Asegura que se devuelve un estado 201 CREATED al agregar una credencial exitosamente.
     */
    @Test
    public void agregarCredencialTest() throws Exception {
        // Arrange
        when(credencialService.save(any(Credencial.class))).thenReturn(credencial);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/credenciales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credencial))) // Convertir Credencial a JSON
                .andExpect(status().isCreated())
                .andExpect(content().string("Credencial creada con éxito."));
    }

    /**
     * Prueba que verifica la actualización de una credencial existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarCredencialTest() throws Exception {
        // Arrange
        when(credencialService.update(any(Credencial.class), eq(id))).thenReturn(credencial);

        // Act & Assert
        mockMvc.perform(put("/api-administrador/v1/credenciales/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credencial))) // Convertir Credencial a JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de una credencial existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarCredencialTest() throws Exception {
        // Arrange
        doNothing().when(credencialService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-administrador/v1/credenciales/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Credencial eliminada con éxito."));
    }

    /**
     * Prueba que verifica el inicio de sesión exitoso.
     * Asegura que se devuelve un estado 200 OK y un mensaje de éxito.
     */
    @Test
    public void loginTest() throws Exception {
        // Arrange
        Login login = new Login("correo@ejemplo.com", "ContraseniaValida");
        when(credencialService.verificarCredenciales(login.getCorreo(), login.getContrasenia())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/credenciales/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))) // Convertir Login a JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Login exitoso"));
    }

    /**
     * Prueba que verifica la asignación de un rol a una credencial.
     * Asegura que se devuelve un estado 200 OK y un mensaje de éxito.
     */
    @Test
    public void asignarRolTest() throws Exception {
        // Arrange
        Integer credencialId = 1;
        Integer rolId = 1;
        doNothing().when(credencialService).asignarRol(credencialId, rolId);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/credenciales/{credencialId}/asignar-rol/{rolId}", credencialId, rolId))
                .andExpect(status().isOk())
                .andExpect(content().string("Rol asignado a la credencial exitosamente"));
    }

    //ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay credenciales registradas.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_CredencialesNoExistentes() throws Exception {
        // Arrange
        when(credencialService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/credenciales"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar una credencial que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarCredencialTest_CredencialNoExistente() throws Exception {
        // Arrange
        when(credencialService.findByID(id)).thenThrow(new NoSuchElementException("Credencial no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/credenciales/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Credencial no encontrada"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar una credencial.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarCredencialTest_Error() throws Exception {
        // Arrange
        when(credencialService.save(any(Credencial.class))).thenThrow(new RuntimeException("Error al crear la credencial"));

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/credenciales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credencial))) // Convertir Credencial a JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear la credencial"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar una credencial que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarCredencialTest_CredencialNoExistente() throws Exception {
        // Arrange
        when(credencialService.update(any(Credencial.class), eq(id))).thenThrow(new NoSuchElementException("Credencial no encontrada"));

        // Act & Assert
        mockMvc.perform(put("/api-administrador/v1/credenciales/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credencial))) // Convertir Credencial a JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Credencial no encontrada"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar una credencial que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarCredencialTest_CredencialNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Credencial no encontrada")).when(credencialService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-administrador/v1/credenciales/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Credencial no encontrada"));
    }

    /**
     * Prueba que verifica el inicio de sesión fallido.
     * Asegura que se devuelve un estado 401 UNAUTHORIZED y un mensaje de error.
     */
    @Test
    public void loginTest_Fallido() throws Exception {
        // Arrange
        Login login = new Login("correo@ejemplo.com", "ContraseniaIncorrecta");
        when(credencialService.verificarCredenciales(login.getCorreo(), login.getContrasenia())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/credenciales/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))) // Convertir Login a JSON
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales incorrectas"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar asignar un rol a una credencial que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void asignarRolTest_CredencialNoEncontrada() throws Exception {
        // Arrange
        Integer credencialId = 1;
        Integer rolId = 1;
        doThrow(new RuntimeException("Credencial no encontrada"))
                .when(credencialService).asignarRol(credencialId, rolId);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/credenciales/{credencialId}/asignar-rol/{rolId}", credencialId, rolId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Credencial no encontrada"));
    }
}