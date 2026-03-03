package com.nomelestar.mimido;

import tools.jackson.databind.ObjectMapper;
import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChefE2ETest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testChefCrudOperations() throws Exception {
                // 1. Create a new Chef
                ChefCreateDTO createDTO = new ChefCreateDTO("Gordon Ramsay", "British Cuisine", "Famous Chef");
                MvcResult createResult = mockMvc.perform(post("/api/chefs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDTO)))
                                .andExpect(status().isCreated())
                                .andReturn();

                String createResponseStr = createResult.getResponse().getContentAsString();
                ChefResponseDTO createdChef = objectMapper.readValue(createResponseStr, ChefResponseDTO.class);

                assertThat(createdChef).isNotNull();
                assertThat(createdChef.id()).isNotNull();
                assertThat(createdChef.name()).isEqualTo("Gordon Ramsay");

                Long chefId = createdChef.id();

                // 2. Read the created Chef
                mockMvc.perform(get("/api/chefs/" + chefId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Gordon Ramsay"));

                // 3. Update the Chef
                ChefUpdateDTO updateDTO = new ChefUpdateDTO("Gordon Ramsay", "French Cuisine", "Updated Description");
                mockMvc.perform(put("/api/chefs/" + chefId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.specialty").value("French Cuisine"));

                // 4. Delete the Chef
                mockMvc.perform(delete("/api/chefs/" + chefId))
                                .andExpect(status().isNoContent());

                // 5. Verify the Chef is deleted
                mockMvc.perform(get("/api/chefs/" + chefId))
                                .andExpect(status().isNotFound());
        }
}
