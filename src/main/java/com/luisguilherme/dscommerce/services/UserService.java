package com.luisguilherme.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luisguilherme.dscommerce.dto.UserDTO;
import com.luisguilherme.dscommerce.entities.Role;
import com.luisguilherme.dscommerce.entities.User;
import com.luisguilherme.dscommerce.projections.UserDetailsProjection;
import com.luisguilherme.dscommerce.repositories.UserRepository;
import com.luisguilherme.dscommerce.util.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	CustomUserUtil customUserUtil;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if(result.size() == 0) {
			throw new UsernameNotFoundException("Username Not Found");
		}
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		
		for(UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		return user;
	}
	
	protected User authenticated() {
		try {			
			String username = customUserUtil.getLoggedUsername();
			return repository.findByEmail(username).get();			
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
		}
		
	}
	
	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		return new UserDTO(user);
	}

}
