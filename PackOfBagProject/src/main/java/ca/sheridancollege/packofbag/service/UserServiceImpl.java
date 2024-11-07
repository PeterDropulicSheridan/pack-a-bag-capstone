package ca.sheridancollege.packofbag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.packofbag.dto.UserDto;
import ca.sheridancollege.packofbag.model.User;
import ca.sheridancollege.packofbag.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	
	private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}

	@Override
	public User save(UserDto userDto) {
		
		User user = new User(0, userDto.getUsername(),passwordEncoder.encode(userDto.getPassword()),userDto.getFullname(), userDto.getEmail());
		
		return userRepository.save(user);
	}


	
	
	

}
