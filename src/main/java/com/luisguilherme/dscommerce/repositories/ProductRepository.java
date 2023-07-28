package com.luisguilherme.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luisguilherme.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
