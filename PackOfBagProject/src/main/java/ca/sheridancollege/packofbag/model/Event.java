package ca.sheridancollege.packofbag.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NonNull;



@Entity
@Table(name="events")

public class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NonNull
	private String date;
	@NonNull
	private String time;
	@NonNull
	private String title;
	@NonNull
	private String description;
	@NonNull
	private String location;
	
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEvent> userEvents = new ArrayList<>();
	
	
	 public Event() {
	        // Default constructor
	    }
	 
		public Event(Long id, @NonNull String date, @NonNull String time, @NonNull String title,
				@NonNull String description, @NonNull String location) {
			super();
			this.id = id;
			this.date = date;
			this.time = time;
			this.title = title;
			this.description = description;
			this.location = location;
		}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	

	
	
}
