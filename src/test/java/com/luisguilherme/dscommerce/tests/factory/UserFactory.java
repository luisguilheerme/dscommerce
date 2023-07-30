package com.luisguilherme.dscommerce.tests.factory;

import java.time.LocalDate;

import com.luisguilherme.dscommerce.entities.Role;
import com.luisguilherme.dscommerce.entities.User;

public class UserFactory {
	
	public static User createClientUser() {
		User user = new User(1L, "Maria", "maria@gmail.com", "98989898", LocalDate.parse("2001-07-25"), "12345");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		return user;
	}
	
	public static User createAdminUser() {
		User user = new User(1L, "Alex", "alex@gmail.com", "98989898", LocalDate.parse("2001-07-25"), "12345");
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		return user;
	}
	
	public static User createCustomClientUser(Long id, String name) {
		User user = new User(id, name, "maria@gmail.com", "98989898", LocalDate.parse("2001-07-25"), "12345");
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		return user;
	}

}
