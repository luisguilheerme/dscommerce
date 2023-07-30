package com.luisguilherme.dscommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luisguilherme.dscommerce.dto.UserDTO;
import com.luisguilherme.dscommerce.entities.User;
import com.luisguilherme.dscommerce.projections.UserDetailsProjection;
import com.luisguilherme.dscommerce.repositories.UserRepository;
import com.luisguilherme.dscommerce.tests.factory.UserDetailsFactory;
import com.luisguilherme.dscommerce.tests.factory.UserFactory;
import com.luisguilherme.dscommerce.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private CustomUserUtil customUserUtil;
	
	private String existingUsername;
	private String nonExistingUsername;
	private User user;
	
	private List<UserDetailsProjection> userDetails;
	
	@BeforeEach
	private void setUp() throws Exception {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "fulano@gmail.com";
		
		user = UserFactory.createClientUser();
		userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);
		
		Mockito.when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(new ArrayList<>());			
		
		Mockito.when(repository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByEmail(nonExistingUsername)).thenReturn(Optional.empty());

	}
	
	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		
		UserDetails result = service.loadUserByUsername(existingUsername);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUsername);
	}
	
	@Test
	public void loadUserByUsernameShouldReturnUsernameNotFoundExceptionWhenUserDoesNotExists() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingUsername);
		});
	}
	
	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		
		Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);
		
		User user = service.authenticated();
		
		Assertions.assertNotNull(user);
		Assertions.assertEquals(user.getUsername(), existingUsername);		
		
	}
	
	@Test
	public void authenticatedShouldReturnUsernameNotFoundExceptionWhenUserExists() {
		
		Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});	
		
	}
	
	@Test
	public void getMeShouldReturnUserDTOWhenUserAuthenticated() {
		
		UserService spyUserService = Mockito.spy(service);
		
		Mockito.doReturn(user).when(spyUserService).authenticated();
		
		UserDTO dto = spyUserService.getMe();
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getEmail(), existingUsername);
	}
	
	@Test
	public void getMeShouldReturnUsernameNotFoundExceptionWhenUserNotAuthenticated() {
		
		UserService spyUserService = Mockito.spy(service);
		
		Mockito.doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			UserDTO dto = spyUserService.getMe();
		});	
		
	}
}
