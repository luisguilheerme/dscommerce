package com.luisguilherme.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luisguilherme.dscommerce.entities.User;
import com.luisguilherme.dscommerce.services.exceptions.ForbiddenException;
import com.luisguilherme.dscommerce.tests.factory.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {
	
	@InjectMocks
	private AuthService service;
	
	@Mock
	private UserService userService;
	
	private User admin, selfClient, otherClient;
	
	
	@BeforeEach
	void setUp() {
		admin = UserFactory.createAdminUser();
		selfClient = UserFactory.createCustomClientUser(1L, "Bob");
		otherClient = UserFactory.createCustomClientUser(2L, "Ana");

	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenAdminLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(admin);
		
		Long userId = admin.getId();
		
		Assertions.assertDoesNotThrow(() -> {
			service.validateSelfOrAdmin(userId);
		});
	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId = selfClient.getId();
		
		Assertions.assertDoesNotThrow(() -> {
			service.validateSelfOrAdmin(userId);
		});
	}

	@Test
	public void validateSelfOrAdminShouldReturnForbiddenExceptionWhenOtherClientLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId = otherClient.getId();
		
		Assertions.assertThrows(ForbiddenException.class, () -> {
			service.validateSelfOrAdmin(userId);
		});
	}
}
