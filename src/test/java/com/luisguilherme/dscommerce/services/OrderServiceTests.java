package com.luisguilherme.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luisguilherme.dscommerce.dto.OrderDTO;
import com.luisguilherme.dscommerce.entities.Order;
import com.luisguilherme.dscommerce.entities.OrderItem;
import com.luisguilherme.dscommerce.entities.Product;
import com.luisguilherme.dscommerce.entities.User;
import com.luisguilherme.dscommerce.repositories.OrderItemRepository;
import com.luisguilherme.dscommerce.repositories.OrderRepository;
import com.luisguilherme.dscommerce.repositories.ProductRepository;
import com.luisguilherme.dscommerce.services.exceptions.ForbiddenException;
import com.luisguilherme.dscommerce.services.exceptions.ResourceNotFoundException;
import com.luisguilherme.dscommerce.tests.factory.OrderFactory;
import com.luisguilherme.dscommerce.tests.factory.ProductFactory;
import com.luisguilherme.dscommerce.tests.factory.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
	
	@InjectMocks
	private OrderService service;
	
	@Mock
	private OrderRepository repository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private UserService userService;
	
	@Mock
	private AuthService authService;
	
	private Long existingOrderId, nonExistingOrderId, existingProductId, nonExistingProductId;
	private Order order;
	private OrderDTO orderDTO;
	private User admin, client;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		existingOrderId = 1L;
		nonExistingOrderId = 2L;
		existingProductId = 1L;
		nonExistingProductId = 2L;
		
		admin = UserFactory.createAdminUser();
		client = UserFactory.createClientUser();
		
		order = OrderFactory.createOrder(client);
		
		orderDTO = new OrderDTO(order);
		product = ProductFactory.createProduct();

		Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(any())).thenReturn(order);
		
		Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList(order.getItems()));
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO dto = service.findById(existingOrderId);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getId(), existingOrderId);		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO dto = service.findById(existingOrderId);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getId(), existingOrderId);		
	}
	
	@Test
	public void findByIdShouldReturnForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
		
		Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ForbiddenException.class, () -> {
			OrderDTO dto = service.findById(existingOrderId);
		});
		
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			OrderDTO dto = service.findById(nonExistingOrderId);
		});
	}
	
	@Test
	public void insertShouldReturnOrderDTOWhenUserLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(client);
		
		OrderDTO dto = service.insert(orderDTO);
		
		Assertions.assertNotNull(dto);
	}
	
	@Test
	public void insertShouldReturnUsernameNotFoundExceptionWhenUserNotLogged() {
		
		Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();
		
		order.setClient(new User());
		orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {				
			OrderDTO dto = service.insert(orderDTO);	
		});	
	}
	
	@Test
	public void insertShouldReturnEntityNotFoundExceptionWhenOrderProductIdDoesNotExists() {
		
		Mockito.when(userService.authenticated()).thenReturn(client);
		
		product.setId(nonExistingProductId);
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);	
		order.getItems().add(orderItem);
		
		orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(EntityNotFoundException.class, () -> {				
			OrderDTO dto = service.insert(orderDTO);	
		});	
	}
	
}
