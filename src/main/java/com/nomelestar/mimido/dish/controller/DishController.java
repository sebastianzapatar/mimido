package com.nomelestar.mimido.dish.controller;

import com.nomelestar.mimido.dish.dto.DishCreateDTO;
import com.nomelestar.mimido.dish.dto.DishResponseDTO;
import com.nomelestar.mimido.dish.dto.DishUpdateDTO;
import com.nomelestar.mimido.dish.model.Dish;
import com.nomelestar.mimido.dish.service.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/dishes")
//todo como paginar, consultar
public class DishController {
    private DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DishResponseDTO postEntity(@Valid @RequestBody DishCreateDTO dish) {
        return dishService.create(dish);
    }
    @GetMapping
    public List<DishResponseDTO> findAll() {
        return dishService.findAll();
    }
    @GetMapping("/{id}")
    public DishResponseDTO findById(@PathVariable Long id) {
        return dishService.findById(id);
    }
    @GetMapping("/by-chef/{chefId}")
    public List<DishResponseDTO> findByChef(@PathVariable Long chefId) {
        return dishService.findByChef(chefId);
    }
    @PutMapping("/{id}")
    public DishResponseDTO updateEntity(@PathVariable Long id, @Valid @RequestBody DishUpdateDTO dish) {
        return dishService.updateById(id, dish);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntity(@PathVariable Long id) {
        dishService.deleteById(id);
    }

}
