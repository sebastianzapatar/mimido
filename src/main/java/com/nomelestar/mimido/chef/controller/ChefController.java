package com.nomelestar.mimido.chef.controller;

import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import com.nomelestar.mimido.chef.service.ChefService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChefResponseDTO create(@Valid @RequestBody ChefCreateDTO dto){
        return chefRepository.create(dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        chefRepository.deleteById(id);
    }
    @PutMapping("/{id}")
    public ChefResponseDTO update(@PathVariable Long id, @Valid @RequestBody
    ChefUpdateDTO dto){
        return chefRepository.update(id, dto);
    }


}
