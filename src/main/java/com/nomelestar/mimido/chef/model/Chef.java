package com.nomelestar.mimido.chef.model;

import com.nomelestar.mimido.dish.model.Dish;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Table(name="chefs")
@Data
public class Chef {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length = 120)
    private String name;
    @Column(nullable=false)
    private String description;
    @Column(nullable=false, length = 80)
    private String speciality;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL)
    private List<Dish> dishes;
}
