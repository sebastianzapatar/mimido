package com.nomelestar.mimido.chef.repository;

import com.nomelestar.mimido.chef.model.Chef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefRepository extends JpaRepository<Chef, Long> {
}
