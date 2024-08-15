package com.aupnmt.service.impl;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	CacheManager cacheManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (Objects.nonNull(cacheManager.getCache("default").get(username))) {
			User user = new User(username, cacheManager.getCache("default").get(username).get().toString(),
					new ArrayList<>());
			//cacheManager.getCache("default").evictIfPresent(username);
			return user;
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}