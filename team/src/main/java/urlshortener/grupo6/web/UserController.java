package urlshortener.grupo6.web;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import urlshortener.common.domain.User;
import urlshortener.common.repository.UserRepository;
import urlshortener.grupo6.security.SignupForm;
import urlshortener.grupo6.security.UserDetailsImpl;
import urlshortener.grupo6.security.UserService;

@RestController
public class UserController {
	
	@Autowired
	protected UserRepository userRepository;	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
	public User viewUser(@PathVariable String username, HttpServletResponse response){
		logger.info("Solicitando datos del usuario: " + username);
		UserDetailsImpl userDet = (UserDetailsImpl) userService.loadUserByUsername(username);
		if (userDet == null) {
			logger.info("Usuario " + username + " no encontrado");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		User user = new User(userDet.getUsername(),userDet.getPassword(), userDet.getEmail());
		logger.info("Usuario consultado: " + user.getUsername() + " -- " + user.getEmail());
		return user;	
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<User> signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result){
		logger.info("Registrando usuario...");
		if (result.hasErrors()){
			logger.info(signupForm.getUsername() + signupForm.getPassword());
			logger.info("Bad request: " + result.getFieldError().toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
        if (userService.isUserExist(signupForm)) {
            logger.info("A User with name " + signupForm.getUsername() + " already exist");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
		userService.signup(signupForm);
		HttpHeaders h = new HttpHeaders();
		try {
			h.setLocation(new URI("/user/" + signupForm.getUsername()));
			logger.info("usuario creado: " + signupForm.getUsername());
			return new ResponseEntity<>(h, HttpStatus.CREATED);
		} catch (URISyntaxException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
