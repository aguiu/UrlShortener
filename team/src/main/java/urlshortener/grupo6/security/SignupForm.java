package urlshortener.grupo6.security;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SignupForm {
	@NotNull
	@Size(min=1, max=100, message="Name length Error")
	private String username;
	
	@NotNull
	@Size(min=6, max=30, message="Password length Error")
	private String password;
	
	@NotNull
	@Size(min=1, max=100, message="Email Length Error")
	@Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message="Email Format Error")
	private String email;
	
	public SignupForm(String username, String password, String email){
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getEmail(){
		return email;
	}
}
