package com.nomelestar.mimido.chef.controller;

import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import com.nomelestar.mimido.chef.service.ChefService;
import com.nomelestar.mimido.common.exception.NotFound;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(ChefController.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false) // por si luego metes Security
class ChefControllerTest {

    @Autowired MockMvc mvc;
    @Autowired
    tools.jackson.databind.ObjectMapper objectMapper;

    @MockitoBean
    ChefService chefService;

    @Test
    void getChefs_ok_200_y_listaJson() throws Exception {
        var c1 = new ChefResponseDTO(1L, "Italiana", "Mario", "Top chef", List.of(10L, 11L));
        var c2 = new ChefResponseDTO(2L, "Mexicana", "Lupe", "Salsas", List.of());

        Mockito.when(chefService.findAll()).thenReturn(List.of(c1, c2));

        mvc.perform(get("/api/chefs"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Mario"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].specialty").value("Mexicana"));
    }

    @Test
    void getChef_ok_200_y_objetoJson() throws Exception {
        var c = new ChefResponseDTO(7L, "Asiática", "Kenji", "Ramen", List.of(1L, 2L));

        Mockito.when(chefService.findById(7L)).thenReturn(c);

        mvc.perform(get("/api/chefs/7"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Kenji"))
                .andExpect(jsonPath("$.dishIds.length()").value(2));
    }

    @Test
    void getChef_notFound_si_servicio_lanza_NotFound() throws Exception {
        Mockito.when(chefService.findById(99L)).thenThrow(new NotFound("Chef not found"));

        // Si tienes un @ControllerAdvice que mapea NotFound -> 404, esto pasará.
        // Si NO lo tienes, Spring responderá 500. (Te dejo abajo la solución recomendada.)
        mvc.perform(get("/api/chefs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_ok_201_y_devuelveChef() throws Exception {
        var req = new ChefCreateDTO("Ana", "Peruana", "Ceviches");
        var resp = new ChefResponseDTO(3L, "Peruana", "Ana", "Ceviches", List.of());

        Mockito.when(chefService.create(any(ChefCreateDTO.class))).thenReturn(resp);

        mvc.perform(post("/api/chefs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Ana"))
                .andExpect(jsonPath("$.specialty").value("Peruana"));
    }

    @Test
    void create_badRequest_400_si_name_vacio_por_validacion() throws Exception {
        // name en blanco viola @NotBlank
        var req = new ChefCreateDTO(" ", "Italiana", "Desc");

        mvc.perform(post("/api/chefs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ok_200_y_devuelveChefActualizado() throws Exception {
        var req = new ChefUpdateDTO("Nuevo Nombre", "Fusión", "Nueva desc");
        var resp = new ChefResponseDTO(5L, "Fusión", "Nuevo Nombre", "Nueva desc", List.of(9L));

        Mockito.when(chefService.update(eq(5L), any(ChefUpdateDTO.class))).thenReturn(resp);

        mvc.perform(put("/api/chefs/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Nuevo Nombre"))
                .andExpect(jsonPath("$.specialty").value("Fusión"));
    }

    @Test
    void update_badRequest_400_si_name_corto() throws Exception {
        // min=2
        var req = new ChefUpdateDTO("A", "Fusión", "x");

        mvc.perform(put("/api/chefs/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_ok_204() throws Exception {
        mvc.perform(delete("/api/chefs/8"))
                .andExpect(status().isNoContent());

        Mockito.verify(chefService).deleteById(8L);
    }

    @Test
    void delete_notFound_si_servicio_lanza_NotFound() throws Exception {
        Mockito.doThrow(new NotFound("Chef not found")).when(chefService).deleteById(44L);

        mvc.perform(delete("/api/chefs/44"))
                .andExpect(status().isNotFound());
    }
}