package com.luisguilherme.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luisguilherme.dscommerce.dto.ProductDTO;
import com.luisguilherme.dscommerce.dto.ProductMinDTO;
import com.luisguilherme.dscommerce.entities.Product;
import com.luisguilherme.dscommerce.repositories.ProductRepository;
import com.luisguilherme.dscommerce.services.exceptions.DatabaseException;
import com.luisguilherme.dscommerce.services.exceptions.ResourceNotFoundException;
import com.luisguilherme.dscommerce.tests.factory.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private String productName;
	private Product product;
	private ProductDTO productDTO;
	private PageImpl<Product> page;
	
	@BeforeEach
	private void SetUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		productName = "Playstation 5";
		
		product = ProductFactory.createProduct(productName);
		page = new PageImpl<>(List.of(product));
		productDTO = new ProductDTO(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.findAll((Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

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
	
	@Test
	public void findAllShouldReturnPagedProductMinDTO() {
		
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<ProductMinDTO> result = service.findAll(productName, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getName(), productName);
	}
	
	@Test
	public void insertShouldReturnProductDTO() {
		
		ProductDTO result = service.insert(productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), product.getId());		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);	
		Assertions.assertEquals(result.getName(), productDTO.getName());
	}
	
	@Test
	public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
			
		});	
	}
	
	@Test
	public void deleteShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldReturnDatabaseExceptionWhenIdExistsAndIsAssociatedWithAnOrder() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
	
}
