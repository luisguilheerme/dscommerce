package com.luisguilherme.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.luisguilherme.dscommerce.dto.OrderDTO;
import com.luisguilherme.dscommerce.entities.Order;
import com.luisguilherme.dscommerce.repositories.OrderRepository;
import com.luisguilherme.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	
	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {		
		Order order = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado") );
		return new OrderDTO(order);
	}
}
