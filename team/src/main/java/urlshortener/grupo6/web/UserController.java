package urlshortener.grupo6.web;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import urlshortener.common.domain.User;
import urlshortener.common.repository.UserRepository;
import urlshortener.common.security.SignupForm;
import urlshortener.common.security.UserDetailsImpl;
import urlshortener.common.security.UserService;

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
	public ModelAndView signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result,
			HttpServletResponse httpServletResponse, ModelAndView model){
		logger.info("Registrando usuario...");
		if (result.hasErrors()){
			logger.info(signupForm.getUsername() + signupForm.getPassword());
			logger.info("Bad request: " + result.getFieldError().toString());
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return model;
		}		
        if (userService.isUserExist(signupForm)) {
            logger.info("A User with name " + signupForm.getUsername() + " already exist");
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
			return model;
        }
		userService.signup(signupForm);
		logger.info("usuario creado: " + signupForm.getUsername());
		httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
		return new ModelAndView("login");
	}
}
