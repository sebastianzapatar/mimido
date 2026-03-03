package com.nomelestar.mimido.chef.service;

import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import com.nomelestar.mimido.common.exception.NotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Carga el contexto de Spring completo (service, repo, entityManager, etc.).
@SpringBootTest

// ✅ Activa el perfil "test": usará application-test.yml y por tanto H2 en memoria.
@ActiveProfiles("test")

// Cada test se ejecuta dentro de una transacción.
// Al terminar cada test, Spring hace rollback automáticamente (no deja datos).
@Transactional
class ChefServiceTest {

    @Autowired
    private ChefService chefService;

    @Test
    void create_and_findById_should_work() {
        // Arrange
        ChefCreateDTO createDTO = new ChefCreateDTO("Gordon", "Gourmet", "Yelling");

        // Act
        ChefResponseDTO createdInfo = chefService.create(createDTO);

        // Assert
        assertNotNull(createdInfo.id());
        assertEquals("Gordon", createdInfo.name());
        assertEquals("Gourmet", createdInfo.specialty());
        assertEquals("Yelling", createdInfo.description());

        // Verify it was correctly saved to the DB
        ChefResponseDTO fetchedInfo = chefService.findById(createdInfo.id());
        assertEquals(createdInfo.id(), fetchedInfo.id());
        assertEquals(createdInfo.name(), fetchedInfo.name());
    }

    @Test
    void findAll_should_return_list_of_chefs() {
        // Arrange
        ChefCreateDTO dto1 = new ChefCreateDTO("Chef1", "Spec1", "Desc1");
        ChefCreateDTO dto2 = new ChefCreateDTO("Chef2", "Spec2", "Desc2");
        chefService.create(dto1);
        chefService.create(dto2);

        // Act
        List<ChefResponseDTO> allChefs = chefService.findAll();

        // Assert
        assertTrue(allChefs.size() >= 2, "There should be at least 2 chefs");
        boolean hasChef1 = allChefs.stream().anyMatch(c -> c.name().equals("Chef1"));
        boolean hasChef2 = allChefs.stream().anyMatch(c -> c.name().equals("Chef2"));
        assertTrue(hasChef1);
        assertTrue(hasChef2);
    }

    @Test
    void findById_should_throw_NotFound_when_not_exists() {
        assertThrows(NotFound.class, () -> chefService.findById(999999L));
    }

    @Test
    void update_should_modify_existing_chef() {
        // Arrange
        ChefCreateDTO createDTO = new ChefCreateDTO("Original Name", "Original Spec", "Original Desc");
        ChefResponseDTO createdInfo = chefService.create(createDTO);

        ChefUpdateDTO updateDTO = new ChefUpdateDTO("Updated Name", "Updated Spec", "Updated Desc");

        // Act
        ChefResponseDTO updatedInfo = chefService.update(createdInfo.id(), updateDTO);

        // Assert
        assertEquals(createdInfo.id(), updatedInfo.id());
        assertEquals("Updated Name", updatedInfo.name());
        assertEquals("Updated Spec", updatedInfo.specialty());
        assertEquals("Updated Desc", updatedInfo.description());

        // Verify update persisted
        ChefResponseDTO fetchedInfo = chefService.findById(createdInfo.id());
        assertEquals("Updated Name", fetchedInfo.name());
    }

    @Test
    void deleteById_should_remove_chef() {
        // Arrange
        ChefCreateDTO createDTO = new ChefCreateDTO("To Delete", "Spec", "Desc");
        ChefResponseDTO createdInfo = chefService.create(createDTO);

        // Act
        chefService.deleteById(createdInfo.id());

        // Assert
        assertThrows(NotFound.class, () -> chefService.findById(createdInfo.id()));
    }
}
