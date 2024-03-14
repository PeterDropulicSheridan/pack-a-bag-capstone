package ca.sheridancollege.packabag.model;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class User {
	private int userId;
	private String username;
	private String password;
	public User() {

	}
	public User(String username2, String password2, List<GrantedAuthority> grantList) {
		// TODO Auto-generated constructor stub
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
