package urlshortener.common.security;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.bind.annotation.ModelAttribute;

public class SignupForm {
	@NotNull
	@Size(min=3, max=100, message="Name length Error")
	private String username;
	
	@NotNull
	@Size(min=6, max=30, message="Password length Error")
	private String password;
	
	@NotNull
	@Size(min=1, max=100, message="Email Length Error")
	@Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message="Email Format Error")
	private String email;
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
}
