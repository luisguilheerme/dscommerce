package com.luisguilherme.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luisguilherme.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
