package com.luisguilherme.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luisguilherme.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
