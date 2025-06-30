package com.SAFE_Rescue.API_Administrador.controller;

import com.SAFE_Rescue.API_Administrador.modelo.Bombero;
import com.SAFE_Rescue.API_Administrador.modelo.Credencial;
import com.SAFE_Rescue.API_Administrador.modelo.Rol;
import com.SAFE_Rescue.API_Administrador.service.BomberoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BomberoController.class)
public class BomberoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BomberoService bomberoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private int rut;
    private Bombero bombero;
    private Integer id;

    @BeforeEach
    public void setUp(){
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        faker = new Faker();
        id = 1;
        rut = faker.number().numberBetween(1000000, 99999999);
        bombero = new Bombero(id, rut, calcularDv(rut),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.name().lastName(),
                new Date(),
                faker.number().numberBetween(100000000, 999999999),
                new Credencial(1, faker.internet().emailAddress(),
                        faker.internet().password(), 0, true,
                        new Rol(1, faker.job().position())));
    }

    // Métodos de prueba exitosos

    /**
     * Prueba que verifica la obtención de todos los bomberos existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de bomberos.
     */
    @Test
    public void listarTest() throws Exception {
        // Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // Ajusta la zona horaria según sea necesario
        String expectedDate = sdf.format(bombero.getFechaRegistro());when(bomberoService.findAll()).thenReturn(List.of(bombero));

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/bomberos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bombero.getId()))
                .andExpect(jsonPath("$[0].run").value(bombero.getRun()))
                .andExpect(jsonPath("$[0].dv").value(bombero.getDv()))
                .andExpect(jsonPath("$[0].nombre").value(bombero.getNombre()))
                .andExpect(jsonPath("$[0].apaterno").value(bombero.getAPaterno()))
                .andExpect(jsonPath("$[0].amaterno").value(bombero.getAMaterno()))
                .andExpect(jsonPath("$[0].fechaRegistro").value(expectedDate))
                .andExpect(jsonPath("$[0].telefono").value(bombero.getTelefono()))
                .andExpect(jsonPath("$[0].credencial").value(bombero.getCredencial()));
    }

    /**
     * Prueba que verifica la búsqueda de un bombero existente por su ID.
     * Asegura que se devuelve un estado 200 OK y el bombero encontrado.
     */
    @Test
    public void buscarBomberoTest() throws Exception {
        // Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // Ajusta la zona horaria según sea necesario
        String expectedDate = sdf.format(bombero.getFechaRegistro());
        when(bomberoService.findByID(id)).thenReturn(bombero);

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/bomberos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bombero.getId()))
                .andExpect(jsonPath("$.run").value(bombero.getRun()))
                .andExpect(jsonPath("$.dv").value(bombero.getDv()))
                .andExpect(jsonPath("$.nombre").value(bombero.getNombre()))
                .andExpect(jsonPath("$.apaterno").value(bombero.getAPaterno()))
                .andExpect(jsonPath("$.amaterno").value(bombero.getAMaterno()))
                .andExpect(jsonPath("$.fechaRegistro").value(expectedDate))
                .andExpect(jsonPath("$.telefono").value(bombero.getTelefono()))
                .andExpect(jsonPath("$.credencial").value(bombero.getCredencial()));
    }

    /**
     * Prueba que verifica la creación de un nuevo bombero.
     * Asegura que se devuelve un estado 201 CREATED al agregar un bombero exitosamente.
     */
    @Test
    public void agregarBomberoTest() throws Exception {
        // Arrange
        when(bomberoService.save(any(Bombero.class))).thenReturn(bombero);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/bomberos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bombero))) // Convertir Bombero a JSON
                .andExpect(status().isCreated())
                .andExpect(content().string("Bombero creado con éxito."));
    }

    /**
     * Prueba que verifica la actualización de un bombero existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarBomberoTest() throws Exception {
        // Arrange
        when(bomberoService.update(any(Bombero.class), eq(id))).thenReturn(bombero);

        // Act & Assert
        mockMvc.perform(put("/api-administrador/v1/bomberos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bombero))) // Convertir Bombero a JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de un bombero existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarBomberoTest() throws Exception {
        // Arrange
        doNothing().when(bomberoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-administrador/v1/bomberos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Bombero eliminado con éxito."));
    }

    /**
     * Prueba que verifica la asignación de una credencial a un bombero.
     * Asegura que se devuelve un estado 200 OK y un mensaje de éxito.
     */
    @Test
    public void asignarCredencialTest() throws Exception {
        // Arrange
        Integer bomberoId = 1;
        Integer credencialId = 1;
        Credencial credencial = new Credencial();
        when(bomberoService.findByID(bomberoId)).thenReturn(bombero);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/bomberos/{bomberoId}/asignar-credencial/{credencialId}", bomberoId, credencialId))
                .andExpect(status().isOk())
                .andExpect(content().string("Credencial asignada al bombero exitosamente"));
    }

    // ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay bomberos registrados.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_BomberosNoExistentes() throws Exception {
        // Arrange
        when(bomberoService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/bomberos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar un bombero que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarBomberoTest_BomberoNoExistente() throws Exception {
        // Arrange
        when(bomberoService.findByID(id)).thenThrow(new NoSuchElementException("Bombero no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api-administrador/v1/bomberos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bombero no encontrado"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar un bombero.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarBomberoTest_Error() throws Exception {
        // Arrange
        when(bomberoService.save(any(Bombero.class))).thenThrow(new RuntimeException("Error al crear el bombero"));

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/bomberos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bombero))) // Convertir Bombero a JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el bombero"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar un bombero que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarBomberoTest_BomberoNoExistente() throws Exception {
        // Arrange
        when(bomberoService.update(any(Bombero.class), eq(id))).thenThrow(new NoSuchElementException("Bombero no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api-administrador/v1/bomberos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bombero))) // Convertir Bombero a JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bombero no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar un bombero que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarBomberoTest_BomberoNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Bombero no encontrado")).when(bomberoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-administrador/v1/bomberos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bombero no encontrado"));
    }


    /**
     * Prueba que verifica el comportamiento al intentar asignar una credencial a un bombero que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void asignarCredencialTest_BomberoNoEncontrado() throws Exception {
        // Arrange
        Integer bomberoId = 1;
        Integer credencialId = 1;
        doThrow(new RuntimeException("Bombero no encontrado"))
                .when(bomberoService).asignarCredencial(bomberoId,credencialId);

        // Act & Assert
        mockMvc.perform(post("/api-administrador/v1/bomberos/{bomberoId}/asignar-credencial/{credencialId}", bomberoId, credencialId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bombero no encontrado"));
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