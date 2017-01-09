package urlshortener.common.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
	UserDetails loadUserByUsername(String username);
	
	void signup(SignupForm signupForm);

	boolean isUserExist(SignupForm signupForm);
}
