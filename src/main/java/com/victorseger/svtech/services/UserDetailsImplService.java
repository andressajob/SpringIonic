package com.victorseger.svtech.services;


import com.victorseger.svtech.config.auth.UserImpl;
import com.victorseger.svtech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsImplService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsImplService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == (null))
			return null;
		return this.userRepository.findByUsername(username.toLowerCase())
				.map(user -> new UserImpl(
				        user.getUsername(),
                        user.getPassword(),
						user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList()))
                ).orElseThrow(()
						-> new UsernameNotFoundException("couldn't find " + username + "!"));
	}
}