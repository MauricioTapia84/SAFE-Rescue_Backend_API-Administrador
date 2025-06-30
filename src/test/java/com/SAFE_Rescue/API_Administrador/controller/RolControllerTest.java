package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.service.RolService;
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


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private Rol rol;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto Rol para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        rol = new Rol(1, faker.job().position());
        id = 1;
    }

    /**
     * Prueba que verifica la obtención de todos los roles existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de roles.
     */
    @Test
    public void listarTest_Roles() throws Exception {
        // Arrange
        when(rolService.findAll()).thenReturn(List.of(rol));

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value(rol.getNombre()));
    }

    /**
     * Prueba que verifica la búsqueda de un rol existente por su ID.
     * Asegura que se devuelve un estado 200 OK y el rol encontrado.
     */
    @Test
    public void buscarRolTest_Rol() throws Exception {
        // Arrange
        when(rolService.findById(id)).thenReturn(rol);

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/roles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value(rol.getNombre()));
    }

    /**
     * Prueba que verifica la creación de un nuevo rol.
     * Asegura que se devuelve un estado 201 CREATED al agregar un rol exitosamente.
     */
    @Test
    public void agregarRolTest() throws Exception {
        // Arrange
        when(rolService.save(any(Rol.class))).thenReturn(rol);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol))) // Convertir Rol a JSON
                .andExpect(status().isCreated())
                .andExpect(content().string("Rol creado con éxito."));
    }

    /**
     * Prueba que verifica la actualización de un rol existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarRolTest() throws Exception {
        // Arrange
        when(rolService.update(any(Rol.class), eq(id))).thenReturn(rol);

        // Act & Assert
        mockMvc.perform(put("/api-administrador/v1/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol))) // Convertir Rol a JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }


    /**
     * Prueba que verifica la eliminación de un rol existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarRolTest() throws Exception {
        // Arrange
        doNothing().when(rolService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-administrador/v1/roles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Rol eliminada con éxito."));
    }

    //ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay roles registrados.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_RolesNoExistentes() throws Exception {
        // Arrange
        when(rolService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/roles"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar un rol que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarRolTest_RolNoExistente() throws Exception {
        // Arrange
        when(rolService.findById(id)).thenThrow(new NoSuchElementException("Rol no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/roles/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rol no encontrado"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar un rol.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarRolTest_Error() throws Exception {
        // Arrange
        when(rolService.save(any(Rol.class))).thenThrow(new RuntimeException("Error al crear el rol"));

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol))) // Convertir Rol a JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el rol"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar un rol que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarRolTest_RolNoExistente() throws Exception {
        // Arrange
        when(rolService.update(any(Rol.class), eq(id))).thenThrow(new NoSuchElementException("Rol no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api-administrador/v1/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol))) // Convertir Rol a JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rol no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar un rol que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarRolTest_RolNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Rol no encontrado")).when(rolService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-administrador/v1/roles/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rol no encontrada"));
    }
}