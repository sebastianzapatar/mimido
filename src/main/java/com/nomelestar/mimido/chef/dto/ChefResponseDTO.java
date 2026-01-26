package com.nomelestar.mimido.chef.dto;

import java.util.List;

public record ChefResponseDTO(
        Long id,
        String specialty,
        String name,
        String description,
        List<Long> dishIds
) {
}
