package com.nomelestar.mimido.chef.service;

import com.nomelestar.mimido.chef.dto.ChefCreateDTO;
import com.nomelestar.mimido.chef.dto.ChefResponseDTO;
import com.nomelestar.mimido.chef.dto.ChefUpdateDTO;
import com.nomelestar.mimido.chef.mapper.ChefMapper;
import com.nomelestar.mimido.chef.model.Chef;
import com.nomelestar.mimido.chef.repository.ChefRepository;
import com.nomelestar.mimido.common.exception.NotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChefService {
    private final ChefRepository chefRepository;
    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }
    public ChefResponseDTO  create(ChefCreateDTO dto) {
        Chef chef = ChefMapper.toEntity(dto);
        Chef saved=chefRepository.save(chef);
        return ChefMapper.toChefResponseDTO(saved);
    }
    public List<ChefResponseDTO> findAll() {
        return chefRepository.findAll().
                stream().
                map(ChefMapper::toChefResponseDTO).
                toList();
    }
    public ChefResponseDTO findById(Long id) {
        Chef chef=chefRepository.findById(id)
                .orElseThrow(()->new NotFound("Chef not found"));
        return ChefMapper.toChefResponseDTO(chef);
    }
    public void deleteById(Long id) {
        Chef chef=chefRepository.findById(id)
                .orElseThrow(()->new NotFound("Chef not found \uD83E\uDD2C"));
        chefRepository.delete(chef);
    }
    public ChefResponseDTO update(Long id,ChefUpdateDTO dto) {
        Chef chef=chefRepository.findById(id)
                .orElseThrow(()->new NotFound("Chef not found 😭"));
        ChefMapper.updateEntity(chef, dto);
        Chef saved=chefRepository.save(chef);
        return ChefMapper.toChefResponseDTO(saved);

    }
    public Chef getEntity(Long id) {
        return chefRepository.findById(id)
                .orElseThrow(()->new NotFound("Not found"));
    }
}
