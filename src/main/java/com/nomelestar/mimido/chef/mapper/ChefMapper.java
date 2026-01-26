package com.nomelestar.mimido.chef.mapper;

import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import com.nomelestar.mimido.chef.model.Chef;
import com.nomelestar.mimido.dish.model.Dish;

import java.util.List;

public class ChefMapper {
    private ChefMapper() {}
    public static ChefResponseDTO toChefResponseDTO(Chef chef) {
        List<Long> dishesIds = chef.getDishes()
                .stream()
                .map(Dish::getId).toList();
        return new ChefResponseDTO(
                chef.getId(),
                chef.getSpecialty(),
                chef.getName(),
                chef.getDescription(),
                dishesIds
        );
    }
    public static Chef toEntity(ChefCreateDTO dto) {
        Chef chef = new Chef();
        chef.setName(dto.name());
        chef.setDescription(dto.description());
        chef.setSpecialty(dto.specialty());
        return chef;
    }
    public static void updateEntity(Chef chef, ChefUpdateDTO dto) {
        chef.setName(dto.name());
        chef.setDescription(dto.description());
        chef.setSpecialty(dto.specialty());
    }

}
