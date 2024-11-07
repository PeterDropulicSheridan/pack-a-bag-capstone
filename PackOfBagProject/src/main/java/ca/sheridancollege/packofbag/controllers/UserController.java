package ca.sheridancollege.packofbag.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sheridancollege.packofbag.dto.UserDto;
import ca.sheridancollege.packofbag.model.Event;
import ca.sheridancollege.packofbag.model.Images;
import ca.sheridancollege.packofbag.model.User;
import ca.sheridancollege.packofbag.model.UserEvent;
import ca.sheridancollege.packofbag.model.Volunteer;
import ca.sheridancollege.packofbag.repository.EventRepository;
import ca.sheridancollege.packofbag.repository.ImageRepository;
import ca.sheridancollege.packofbag.repository.UserEventRepository;
import ca.sheridancollege.packofbag.repository.VolunteerRepository;
import ca.sheridancollege.packofbag.service.EmailService; // Import the EmailService
import ca.sheridancollege.packofbag.service.UserMessageEmailService;
import ca.sheridancollege.packofbag.service.UserService;
import ca.sheridancollege.packofbag.service.VolunteerService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private VolunteerRepository volRepo;
	
	@Autowired
	private UserEventRepository userRepo;
	
	@Autowired
	private EventRepository eveRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService; // Add EmailService
	
	@Autowired
	private VolunteerService volunteerService;
	
	@Autowired
	private ImageRepository imgRepo;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
    private UserMessageEmailService userMessageEmailService;
	
	@GetMapping("/")
public String index(Model model) {
    long volunteerCount = volunteerService.countVolunteers(); // Get the count of volunteers
    int goal = 100; // Set your goal for volunteers
    double progressPercentage = (double) volunteerCount / goal * 100; // Calculate the percentage
    model.addAttribute("volunteerCount", volunteerCount); // Add it to the model
    model.addAttribute("progressPercentage", progressPercentage); // Add progress percentage to the model
    return "index"; // Return the index view
}

	@GetMapping("/thanksto")
	public String ThanksToPage() {

		return "thankTo";
	}
	
	@GetMapping("/whoWeAre")
	public String whowearePage() {

		return "WhoWeAre";
	}
	
	@GetMapping("/contact")
	public String contactPage() {

		return "contact";
	}
	
	@GetMapping("/donations")
	public String DonationPage() {

		return "Donations";
	}
	@GetMapping("/history")
	public String HistoryPage() {

		return "History";
	}
	

@GetMapping("/home")
public String home(Model model, Principal principal) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    model.addAttribute("userdetail", userDetails);
    System.out.println("Authenticated user: " + userDetails.getUsername()); // Log the authenticated user
    model.addAttribute("vol", volRepo.findAll());
    model.addAttribute("eve", eveRepo.findAll());
    
    return "home";
}
	
	



	//events start
	@GetMapping("/event-register")
	public String eventRegister(Model model) {
		model.addAttribute("event", new Event());
		return "Event-Register.html";
	}
	
	@GetMapping("/eventView")
	public String viewEvents(Model model) {
	    List<Event> events = eveRepo.findAll();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    List<Event> sortedEvents = events.stream()
	        .filter(event -> event.getDate() != null && !event.getDate().isEmpty())
	        .sorted(Comparator.comparing(event -> {
	            try {
	                return LocalDate.parse(event.getDate(), formatter);
	            } catch (DateTimeParseException e) {
	                return LocalDate.MAX;
	            }
	        }))
	        .collect(Collectors.toList());

	    model.addAttribute("eve", sortedEvents);
	    return "eventView";
	}

	
	@PostMapping("/event-register")
	public String processEventAdd(@ModelAttribute Event event) {
		eveRepo.save(event);
		return"event-register";
	}
	
	@GetMapping("/editEvents")
	public String editEvent(Model model) throws JsonProcessingException {
		List<Event> events = eveRepo.findAll();
		model.addAttribute("eve", events);
		return "editEvents";
	}
	
	@PostMapping("/delete-event")
	public String deleteEventById(@RequestParam("id") Long id) {
	    eveRepo.deleteById(id);
	    return "redirect:/editEvents";
	}
	
	@GetMapping("/edit-events/{id}")
	public String editEvents(@PathVariable("id") Long id, Model model) {
	    Optional<Event> eventOptional = eveRepo.findById(id);
	    if (eventOptional.isPresent()) {
	        model.addAttribute("event", eventOptional.get());
	        return "eventEdit";
	    } else {
	        model.addAttribute("error", "Volunteer not found");
	        return "eventView"; 
	    }
	}
	

	
	
	
	@PostMapping("/edit-events/{id}")
public String editEvent(@PathVariable Long id, @ModelAttribute Event updatedEvent) {
    // Find the existing event
    Event existingEvent = eveRepo.findById(id).orElse(null);
    if (existingEvent != null) {
        // Update the event details
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setDate(updatedEvent.getDate());
        existingEvent.setTime(updatedEvent.getTime());
        existingEvent.setLocation(updatedEvent.getLocation());

        // Save the updated event
        eveRepo.save(existingEvent);

        // Update the eventTitle in UserEvent
        List<UserEvent> userEvents = userRepo.findByEvent(existingEvent); // Assuming you have a method to find UserEvents by Event
        for (UserEvent userEvent : userEvents) {
            userEvent.setEventTitle(updatedEvent.getTitle()); // Update the event title
            userRepo.save(userEvent); // Save the updated UserEvent
        }
    }
    return "redirect:/eventView"; // Redirect to the event view or another appropriate page
}
	

	
	//events ends here
	
	
	//volunteer info Starts here
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
	
	@GetMapping("/userEventRegistration/{eventId}")
	public String userEventRegister(@PathVariable Long eventId, Model model) {
	    Event event = eveRepo.findById(eventId).orElse(null);
	    if (event == null) {
	        // Add logging
	        System.out.println("Event not found with ID: " + eventId);
	        return "redirect:/eventView"; // Redirect if event not found
	    }
	    model.addAttribute("event", event);
	    model.addAttribute("userEvent", new UserEvent());
	    return "userEventRegistration"; 
	}

	
	@PostMapping("/userEventRegistration")
	public String registerUserEvent(@ModelAttribute UserEvent userEvent, @RequestParam Long eventId, RedirectAttributes redirectAttributes) {
	    Event event = eveRepo.findById(eventId).orElse(null);
	    if (event != null) {
	        // Check for duplicate email or phone number
	        List<UserEvent> existingUserEvents = userRepo.findAll();
	        boolean isDuplicate = existingUserEvents.stream().anyMatch(existingUserEvent -> 
	            existingUserEvent.getEmail().equals(userEvent.getEmail()) || 
	            existingUserEvent.getPhoneNumber().equals(userEvent.getPhoneNumber())
	        );

	        if (isDuplicate) {
	            redirectAttributes.addFlashAttribute("duplicateError", "Email or phone number is already registered.");
	            return "redirect:/userEventRegistration/" + eventId; // Redirect back to the registration page
	        }

	        userEvent.setEvent(event);
	        userEvent.setEventTitle(event.getTitle());
	        emailService.sendRegistrationEmail(userEvent.getEmail(), event.getTitle(), event.getTime(), event.getDate(), event.getLocation());
	    } else {
	        System.out.println("Event not found for ID: " + eventId);
	        return "redirect:/eventView";
	    }
	    
	    userRepo.save(userEvent);
	    redirectAttributes.addFlashAttribute("eventTitle", event.getTitle());
	    return "redirect:/eventRegistrationSuccess";
	}

	
	@GetMapping("/userEventView")
	public String userEventView(Model model) {
	    // Fetch all UserEvent objects from the database
	    List<UserEvent> userEvents = userRepo.findAll();
	    
	    // Add the list of UserEvents to the model
	    model.addAttribute("userEvents", userEvents);
	    
	    // Return the view name
	    return "userEventView"; // This should be the name of your Thymeleaf view (userEventView.html)
	}
	
	





	
	@PostMapping("/delete-volunteer")
	public String deleteVolunteerById(@RequestParam("id") Long id) {
	    volRepo.deleteById(id);
	    return "redirect:/volunteerView";
	}
	

	@GetMapping("/edit-volunteer/{id}")
	public String editVolunteer(@PathVariable("id") Long id, Model model) {
	    Optional<Volunteer> volunteerOptional = volRepo.findById(id);
	    if (volunteerOptional.isPresent()) {
	        model.addAttribute("volunteer", volunteerOptional.get());
	        return "editVolunteer";
	    } else {
	        model.addAttribute("error", "Volunteer not found");
	        return "volunteerView"; 
	    }
	}
	
	@PostMapping("/edit-volunteer/{id}")
	public String updateVolunteer(@PathVariable("id") Long id, @ModelAttribute Volunteer updatedVolunteer) {
	    Optional<Volunteer> volunteerOptional = volRepo.findById(id);
	    if (volunteerOptional.isPresent()) {
	        Volunteer volunteer = volunteerOptional.get();
	        volunteer.setFirstName(updatedVolunteer.getFirstName());
	        volunteer.setLastName(updatedVolunteer.getLastName());
	        volunteer.setAge(updatedVolunteer.getAge()); // Update age
	        volunteer.setPhoneNumber(updatedVolunteer.getPhoneNumber()); // Update phone number
	        volunteer.setEmail(updatedVolunteer.getEmail()); // Update email
	        volunteer.setAddress(updatedVolunteer.getAddress()); // Update address
	        volunteer.setCity(updatedVolunteer.getCity()); // Update city
	        volunteer.setProvince(updatedVolunteer.getProvince()); // Update province
	        volunteer.setPostalCode(updatedVolunteer.getPostalCode()); // Update postal code

	        volRepo.save(volunteer);
	    }
	    return "redirect:/volunteerView";
	}
	//volunteer ends here
	
	
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
	
	//LOC review
	@PostMapping("/register")
	public String registersave(@ModelAttribute("user") UserDto userDto, Model model) {
		User user  = userService.findByUsername(userDto.getUsername());
		if(user != null) {
			model.addAttribute("userexist",user);
			return "register";
		}
		userService.save(userDto);
		return "redirect:/register?success"; //Success handled in the HTML
		
	}
	
	
	@GetMapping("/eventRegistrationSuccess")
	public String showEventRegistrationSuccess(Model model) {
	    return "eventRegistrationSuccess";
	}
	
	
	// Image Upload page with dynamic event names from Images
	@GetMapping("/imageUpload")
	public String imageUpload(Model model) {
	    List<Images> images = imgRepo.findAll(); // Get all images from the database
	    List<String> eventNames = images.stream()
	        .map(Images::getEventName)
	        .filter(eventName -> eventName != null && !eventName.isEmpty()) // Filter out null or empty event names
	        .distinct() // Get distinct event names
	        .collect(Collectors.toList());
	    
	    model.addAttribute("eventNames", eventNames); // Pass event names to the view
	    return "ImageUpload";
	}


	// Handle Image Upload with optional event association
	@PostMapping("/imagePostUpload")
	public String imageUpload(@RequestParam("img") MultipartFile[] imgs,
	                          @RequestParam(value = "eventName", required = false) String eventName, 
	                          @RequestParam(value = "newEvent", required = false) String newEvent, 
	                          HttpSession session) {
	    // Clear any previous error message
	    session.removeAttribute("msg");

	    for (MultipartFile img : imgs) {
	        // Validate image format
	        String fileName = img.getOriginalFilename();
	        if (fileName == null || !(fileName.toLowerCase().endsWith(".jpg") || 
	                                  fileName.toLowerCase().endsWith(".jpeg") || 
	                                  fileName.toLowerCase().endsWith(".png"))) {
	            // Redirect back to the upload page with an error parameter
	            return "redirect:/imageUpload?error=Invalid file type. Only JPG and PNG are allowed.";
	        }

	        Images im = new Images();
	        im.setImageName(fileName);

	        // Use new event name if provided, otherwise use the selected event
	        if (newEvent != null && !newEvent.isEmpty()) {
	            im.setEventName(newEvent);
	        } else {
	            im.setEventName(eventName);
	        }

	        // Save the image entity
	        Images uploadImg = imgRepo.save(im);

	        if (uploadImg != null) {
	            try {
	                File saveFile = new ClassPathResource("static/img").getFile();
	                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);
	                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    session.setAttribute("msg", "Images Uploaded Successfully");
	    return "redirect:/imageUpload";
	}



	// Photo gallery view with dynamic event list from Images
	@GetMapping("/photoGallery")
	public String photoGallery(@RequestParam(value = "filter", required = false, defaultValue = "ALL") String filter, Model model) {
	    List<Images> allImages = imgRepo.findAll();
	    
	    // Get distinct event names, including "MISC" if needed
	    List<String> events = allImages.stream()
	        .map(image -> image.getEventName() == null || image.getEventName().isEmpty() ? "MISC" : image.getEventName())
	        .distinct()
	        .collect(Collectors.toList());

	    List<Images> filteredImages;

	    // Apply filtering based on the selected filter
	    if ("ALL".equalsIgnoreCase(filter)) {
	        filteredImages = allImages;  // No filtering, show all images
	    } else if ("MISC".equalsIgnoreCase(filter)) {
	        // Retrieve images with null or empty event names
	        filteredImages = allImages.stream()
	            .filter(image -> image.getEventName() == null || image.getEventName().isEmpty())
	            .collect(Collectors.toList());
	    } else {
	        // Filter by specific event name
	        filteredImages = imgRepo.findByEventName(filter);
	    }

	    // Update model attributes
	    model.addAttribute("images", filteredImages);  // Use filtered images
	    model.addAttribute("events", events);
	    model.addAttribute("filter", filter);  // Pass the filter to the model for default selection
	    
	    return "photoGallery";
	}

	@PostMapping("/filterImages")
	public String filterImages(@RequestParam("filter") String filter, Model model) {
	    List<Images> filteredImages;

	    // Handle "ALL" to retrieve all images without any filtering
	    if ("ALL".equalsIgnoreCase(filter)) {
	        filteredImages = imgRepo.findAll();
	    } else if ("MISC".equalsIgnoreCase(filter)) {
	        // Retrieve images with null or empty event names
	        filteredImages = imgRepo.findAll().stream()
	            .filter(image -> image.getEventName() == null || image.getEventName().isEmpty())
	            .collect(Collectors.toList());
	    } else {
	        // Filter by specific event name
	        filteredImages = imgRepo.findByEventName(filter);
	    }

	    // Ensure "MISC" is included in the events list if there are images without an event name
	    List<String> events = imgRepo.findAll().stream()
	        .map(image -> image.getEventName() == null || image.getEventName().isEmpty() ? "MISC" : image.getEventName())
	        .distinct()
	        .collect(Collectors.toList());

	    model.addAttribute("images", filteredImages);
	    model.addAttribute("events", events);

	    return "photoGallery";
	    
	}
	
	



	@PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String name, @RequestParam String email, @RequestParam String message, RedirectAttributes redirectAttributes) {
        userMessageEmailService.sendContactEmail(name, email, message);
        redirectAttributes.addFlashAttribute("successMessage", "Your message has been sent successfully!");
        return "redirect:/contact"; // Redirect back to the contact page
    }
	
	@PostMapping("/delete-image")
	public String deleteImageById(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
	    Optional<Images> imageOptional = imgRepo.findById(id);
	    if (imageOptional.isPresent()) {
	        imgRepo.deleteById(id);
	        redirectAttributes.addFlashAttribute("msg", "Image deleted successfully.");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "Image not found.");
	    }
	    return "redirect:/photoGallery";
	}


	@PostMapping("/delete-image/{id}")
	public String deleteImage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		imgRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Image deleted successfully.");
		return "redirect:/photoGallery"; // Redirect back to the photo gallery
	}

}


