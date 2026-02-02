package com.nomelestar.mimido.dish.service;

import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.mapper.ChefMapper;
import com.nomelestar.mimido.chef.model.Chef;
import com.nomelestar.mimido.chef.service.ChefService;
import com.nomelestar.mimido.common.exception.NotFound;
import com.nomelestar.mimido.dish.dto.DishCreateDTO;
import com.nomelestar.mimido.dish.dto.DishResponseDTO;
import com.nomelestar.mimido.dish.dto.DishUpdateDTO;
import com.nomelestar.mimido.dish.mapper.DishMapper;
import com.nomelestar.mimido.dish.model.Dish;
import com.nomelestar.mimido.dish.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final ChefService chefService;

    public DishService(DishRepository dishRepository, ChefService chefService) {
        this.dishRepository = dishRepository;
        this.chefService = chefService;
    }
    public DishResponseDTO create(DishCreateDTO dto){

        Chef chef=chefService.getEntity(dto.chefId());
        Dish dish= DishMapper.toEntity(dto,chef);
        dishRepository.save(dish);
        return DishMapper.toResponseDTO(dish);
    }
    public DishResponseDTO findById(Long id) {
        Dish dish=dishRepository.findById(id)
                .orElseThrow(()->new NotFound("Not found"));
        return DishMapper.toResponseDTO(dish);
    }
    public List<DishResponseDTO> findAll(){
        List<Dish> dishes=dishRepository.findAll();
        return dishes.stream().map(DishMapper::toResponseDTO)
                .toList();
    }
    public List<DishResponseDTO> findByChef(Long chefId){
        // todo validar que el chef exista.
        List<Dish> dishes=dishRepository.findByChefId(chefId);
        return dishes.stream().map(DishMapper::toResponseDTO)
                .toList();
    }
    public DishResponseDTO updateById(Long id, DishUpdateDTO dto) {
        Dish dish=dishRepository.findById(id)
                .orElseThrow(()->new NotFound("Not found"));
        Chef chef=chefService.getEntity(dto.chefId());
        dish.setChef(chef);
        dish.setName(dto.name());
        dish.setPrice(dto.price());
        Dish saved=dishRepository.save(dish);
        return DishMapper.toResponseDTO(saved);
    }
    public void deleteById(Long id) {
        Dish dish=dishRepository.findById(id)
                        .orElseThrow(()->new NotFound("Not found"));
        dishRepository.delete(dish);
    }
}
