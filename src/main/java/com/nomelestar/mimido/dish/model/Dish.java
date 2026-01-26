package com.nomelestar.mimido.dish.model;

import com.nomelestar.mimido.chef.model.Chef;
import jakarta.persistence.*;

@Entity
@Table(name="dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="chef_id",nullable = false)
    private Chef chef;
}
