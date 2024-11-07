package ca.sheridancollege.packofbag.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ca.sheridancollege.packofbag.model.User;

import ca.sheridancollege.packofbag.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	
	private UserRepository userRepository;
	
	 @Autowired
	    public CustomUserDetailsService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("Username or password not found");
		}
		return new CustomUserDetail(
				user.getUsername(), 
				user.getPassword(),
				authorities(),
				user.getFullname(),
				user.getEmail()
				);
	}
	
	public Collection<? extends GrantedAuthority > authorities(){
		return Arrays.asList(new SimpleGrantedAuthority("USER"));
	}
	

}
