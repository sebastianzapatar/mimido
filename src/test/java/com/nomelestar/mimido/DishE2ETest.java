package com.nomelestar.mimido;

import tools.jackson.databind.ObjectMapper;
import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.dish.dto.DishCreateDTO;
import com.nomelestar.mimido.dish.dto.DishUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DishE2ETest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testDishCrudOperations() throws Exception {
                // 1. First create a Chef for the dish
                ChefCreateDTO chefCreateDTO = new ChefCreateDTO("Chef for Dish", "General", "Test Chef");
                MvcResult chefResult = mockMvc.perform(post("/api/chefs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(chefCreateDTO)))
                                .andExpect(status().isCreated())
                                .andReturn();

                String chefResponseStr = chefResult.getResponse().getContentAsString();
                // Extract ID using regex or direct JSON parsing
                Long chefId = Long.parseLong(chefResponseStr.replaceAll(".*\"id\":(\\d+).*", "$1"));

                // 2. Create a new Dish
                DishCreateDTO createDTO = new DishCreateDTO("Spaghetti Carbonara", new BigDecimal("15.50"), chefId);
                MvcResult createResult = mockMvc.perform(post("/api/dishes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDTO)))
                                .andExpect(status().isCreated())
                                .andReturn();

                String createResponseStr = createResult.getResponse().getContentAsString();
                Long dishId = Long.parseLong(createResponseStr.replaceAll(".*\"id\":(\\d+).*", "$1"));

                // 3. Read the created Dish
                mockMvc.perform(get("/api/dishes/" + dishId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Spaghetti Carbonara"));

                // 4. Update the Dish
                DishUpdateDTO updateDTO = new DishUpdateDTO("Spaghetti Bolognese", new BigDecimal("18.00"), chefId);
                mockMvc.perform(put("/api/dishes/" + dishId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Spaghetti Bolognese"));

                // 5. Delete the Dish
                mockMvc.perform(delete("/api/dishes/" + dishId))
                                .andExpect(status().isNoContent());

                // 6. Verify the Dish is deleted
                mockMvc.perform(get("/api/dishes/" + dishId))
                                .andExpect(status().isNotFound());
        }
}
