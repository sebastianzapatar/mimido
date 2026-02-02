package com.nomelestar.mimido.dish.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DishUpdateDTO(
        @NotBlank(message = "please please my darling")
        @Size(min=2, max = 140)
        String name,

        @NotBlank(message = "price is required")
        @DecimalMin(value="0.00",inclusive = false,message = "price must be greater than 0")
        @Digits(integer = 8,fraction = 2)
        BigDecimal price,

        @NotNull(message = "chefId is required")
        @Positive(message = "chefId has to be positive mf")
        Long chefId
) {

}
