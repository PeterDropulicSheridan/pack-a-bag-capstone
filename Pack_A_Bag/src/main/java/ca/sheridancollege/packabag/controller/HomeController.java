package ca.sheridancollege.packabag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.packabag.dao.UserRegistrationDatabaseAccess;
import ca.sheridancollege.packabag.model.User;

@Controller
public class HomeController {
	@Autowired
	private UserRegistrationDatabaseAccess da;

	@GetMapping("/index")
	public String index() {
		return "index";

	}

	@GetMapping("/register")
	public String goRegistration() {
		return "register";
	}
	
	@GetMapping("/registerSuccessful")
	public String toGoRegisterSuccess() {
		return "registerSuccessful";
	}

	@PostMapping("/register")
	public String doRegistration(Model model, @RequestParam String username, @RequestParam String password) {
		da.addUser(username, password);
		User user1 = (User) da.findUserAccount(username);
		int userId = user1.getUserId();
		da.addRole(userId, 1);
		java.util.List<User> users = da.getUserId();
		model.addAttribute("users", users);
		return "registerSuccessful";
	}

	@GetMapping("/login")
	public String securityLogin() {
		return "login";
	}

	@GetMapping("/admin/manager")
	public String managerRoleExample() {
		return "/admin/manager";
	}
}
