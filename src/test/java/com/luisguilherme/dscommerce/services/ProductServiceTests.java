package com.luisguilherme.dscommerce.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luisguilherme.dscommerce.dto.ProductDTO;
import com.luisguilherme.dscommerce.entities.Product;
import com.luisguilherme.dscommerce.repositories.ProductRepository;
import com.luisguilherme.dscommerce.services.exceptions.ResourceNotFoundException;
import com.luisguilherme.dscommerce.tests.factory.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private String productName;
	private Product product;
	
	@BeforeEach
	private void SetUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		productName = "Playstation 5";
		
		product = ProductFactory.createProduct(productName);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getName(), product.getName());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundExceptionWhenIdDoesNotExists() {		
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

}
