package com.nomelestar.mimido.chef.mapper;

import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import com.nomelestar.mimido.chef.model.Chef;
import com.nomelestar.mimido.dish.model.Dish;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChefMapperTest {

    @Test
    void toChefResponseDTO_with_null_dishes() {
        Chef chef = new Chef();
        chef.setId(1L);
        chef.setName("Alain");
        chef.setSpecialty("Francesa");
        chef.setDescription("Alta cocina");
        chef.setDishes(null);

        ChefResponseDTO result = ChefMapper.toChefResponseDTO(chef);

        assertEquals(1L, result.id());
        assertEquals("Alain", result.name());
        assertEquals("Francesa", result.specialty());
        assertEquals("Alta cocina", result.description());
        assertNotNull(result.dishIds());
        assertTrue(result.dishIds().isEmpty());
    }

    @Test
    void toChefResponseDTO_with_dishes() {
        Chef chef = new Chef();
        chef.setId(2L);
        chef.setName("Ferran");
        chef.setSpecialty("Molecular");
        chef.setDescription("Espumas");

        Dish d1 = new Dish();
        d1.setId(10L);
        Dish d2 = new Dish();
        d2.setId(20L);

        chef.setDishes(List.of(d1, d2));

        ChefResponseDTO result = ChefMapper.toChefResponseDTO(chef);

        assertEquals(2L, result.id());
        assertEquals("Ferran", result.name());
        assertEquals(2, result.dishIds().size());
        assertTrue(result.dishIds().containsAll(List.of(10L, 20L)));
    }

    @Test
    void toEntity() {
        ChefCreateDTO dto = new ChefCreateDTO("Julia", "Baking", "Child");

        Chef chef = ChefMapper.toEntity(dto);

        assertEquals("Julia", chef.getName());
        assertEquals("Baking", chef.getSpecialty());
        assertEquals("Child", chef.getDescription());
        assertNull(chef.getId());
        assertNull(chef.getDishes());
    }

    @Test
    void updateEntity() {
        Chef chef = new Chef();
        chef.setName("Old");
        chef.setSpecialty("OldSpec");
        chef.setDescription("OldDesc");

        ChefUpdateDTO dto = new ChefUpdateDTO("New", "NewSpec", "NewDesc");

        ChefMapper.updateEntity(chef, dto);

        assertEquals("New", chef.getName());
        assertEquals("NewSpec", chef.getSpecialty());
        assertEquals("NewDesc", chef.getDescription());
    }
}
