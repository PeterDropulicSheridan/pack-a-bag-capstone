package ca.sheridancollege.packabag.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.packabag.dao.UserRegistrationDatabaseAccess;
import ca.sheridancollege.packabag.model.User;

@Service
public class UserDetailServiceImplementation implements UserDetailsService {

	@Autowired
	UserRegistrationDatabaseAccess da;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Find the user based on the username
		User user = da.findUserAccount(username);
		// If the user doesn't exist, throw an exception
		if (user == null) {
			System.out.println("User not found: " + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		// Get a list of roles
		java.util.List<String> roleNames = da.getRolesById(user.getUserId());
		// Change the list of roles into a list of GrantedAuthority
		java.util.List<GrantedAuthority> authorities = new ArrayList<>();
		if (roleNames != null) {
			for (String role : roleNames) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
		}
		// Create a UserDetails object using
		// org.springframework.security.core.userdetails.User
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), authorities);
		return userDetails;
	}

}
