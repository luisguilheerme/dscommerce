package com.luisguilherme.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luisguilherme.dscommerce.entities.OrderItem;
import com.luisguilherme.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{

}
