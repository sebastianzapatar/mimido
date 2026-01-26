package com.nomelestar.mimido.dish.model;

import com.nomelestar.mimido.chef.model.Chef;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="dishes")
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="chef_id",nullable = false)
    private Chef chef;
}
