package com.nomelestar.mimido.chef.controller;

import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.service.ChefService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chefs")
public class ChefController {
    private final ChefService chefRepository;
    public ChefController(ChefService chefRepository) {
        this.chefRepository = chefRepository;
    }
    @GetMapping
    public List<ChefResponseDTO> getChefs() {
        return chefRepository.findAll();
    }
    @GetMapping("/{id}")
    public ChefResponseDTO getChef(@PathVariable Long id) {
        return chefRepository.findById(id);
    }

}
