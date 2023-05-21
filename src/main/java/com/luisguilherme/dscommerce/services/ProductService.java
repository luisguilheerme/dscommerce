package com.luisguilherme.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luisguilherme.dscommerce.dto.ProductDTO;
import com.luisguilherme.dscommerce.entities.Product;
import com.luisguilherme.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {		
		Product product = repository.findById(id).get();
		return new ProductDTO(product);
	}
	

}
