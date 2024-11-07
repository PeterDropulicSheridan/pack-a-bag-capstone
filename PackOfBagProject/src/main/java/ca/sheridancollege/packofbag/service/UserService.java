package ca.sheridancollege.packofbag.service;

import ca.sheridancollege.packofbag.dto.UserDto;
import ca.sheridancollege.packofbag.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

	User findByUsername(String username);
	User save(UserDto userDto);
	
	
	
}
