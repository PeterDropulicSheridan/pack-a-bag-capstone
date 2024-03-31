package ca.sheridancollege.packofbag.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.packofbag.dto.UserDto;
import ca.sheridancollege.packofbag.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ca.sheridancollege.packofbag.model.User;
import ca.sheridancollege.packofbag.model.Volunteer;
import ca.sheridancollege.packofbag.repository.VolunteerRepository;

@Controller
@AllArgsConstructor
public class UserController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private VolunteerRepository volRepo;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/index")
	public String index() {

		return "index";
	}
	@GetMapping("/")
	public String index2() {

		return "index";
	}

	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("userdetail", userDetails);
		model.addAttribute("vol", volRepo.findAll());
		return "home";
	}
	@GetMapping("/volunteerView")
	public String view(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("userdetail", userDetails);
		
		model.addAttribute("vol", volRepo.findAll());
		return "volunteerView";
	}
	
	
	
	@GetMapping("/volunteerSuccess")
	public String Success() {
		
		return "volunteerSuccess";
	}
	
	@GetMapping("/volunteer-register")
	public String volunteerRegister(Model model) {
		model.addAttribute("volunteer", new Volunteer());
		return "volunteer-register.html";
	}
	
	@PostMapping("/volunteer-register")
	public String processAdd(@ModelAttribute Volunteer volunteer) {
		volRepo.save(volunteer);
		return"redirect:/volunteerSuccess";
	}
	
	
	@GetMapping("/login")
	public String login(Model model, UserDto userDto) {
		
		model.addAttribute("user", userDto);
		
		return "login";
		
	}
	
	@GetMapping("/register")
	public String register(Model model, UserDto userDto) {
		model.addAttribute("user", userDto);
		return "register";
		
	}
	
	@PostMapping("/register")
	public String registersave(@ModelAttribute("user") UserDto userDto, Model model) {
		User user  = userService.findByUsername(userDto.getUsername());
		if(user != null) {
			model.addAttribute("userexist",user);
			return "register";
		}
		userService.save(userDto);
		return "redirect:/register?success";
		
	}
	
	@PostMapping("/delete-volunteer")
	public String deleteVolunteerById(@RequestParam("id") Long id) {
	    volRepo.deleteById(id);
	    return "redirect:/volunteerView";
	}
	
	@GetMapping("/edit-volunteer/{id}")
	public String editVolunteer(@PathVariable Long id, Model model) {
	    Optional<Volunteer> volunteerOptional = volRepo.findById(id);
	    if (volunteerOptional.isPresent()) {
	        Volunteer volunteer = volunteerOptional.get();
	        model.addAttribute("volunteer", volunteer);
	        return "editVolunteer";
	    } else {
	        return "redirect:/volunteerView"; 
	    }
	}
	
	@PostMapping("/edit-volunteer/{id}")
	public String updateVolunteer(@PathVariable Long id, @ModelAttribute Volunteer updatedVolunteer) {
	    Optional<Volunteer> volunteerOptional = volRepo.findById(id);
	    if (volunteerOptional.isPresent()) {
	        Volunteer volunteer = volunteerOptional.get();
	
	        volunteer.setFirstName(updatedVolunteer.getFirstName());
	        volunteer.setLastName(updatedVolunteer.getLastName());

	        volRepo.save(volunteer);
	        return "redirect:/volunteerView"; 
	    } else {
	        return "redirect:/volunteerView"; 
	    }
	}

}
