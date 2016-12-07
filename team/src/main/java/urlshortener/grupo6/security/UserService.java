package urlshortener.grupo6.security;

public interface UserService {
	UserDetailsImpl loadUserByUsername(String username);
	
	void signup(SignupForm signupForm);
}
