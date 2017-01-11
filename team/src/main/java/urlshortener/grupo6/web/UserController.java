package urlshortener.grupo6.web;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.client.RestTemplate;
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
	public User viewUser(@PathVariable String username, HttpServletRequest request){
		// Para realizar esta operacion, hay que estar en la whitelist, puesto
		// que devuelve informacion sensible
		String ipConfiable = "127.0.0.1";
		logger.info(ipConfiable + " a " + request.getRemoteAddr());
		if(request.getRemoteAddr().equals(ipConfiable)){
			logger.info("Solicitando datos del usuario: " + username + " desde " + ipConfiable);
			UserDetailsImpl userDet = (UserDetailsImpl) userService.loadUserByUsername(username);
			if (userDet == null) {
				logger.info("Usuario " + username + " no encontrado");
				return null;
			}
			User user = new User(userDet.getUsername(),userDet.getPassword(), userDet.getEmail());
			logger.info("Usuario consultado: " + user.getUsername() + " -- " + user.getEmail());
			return user;
		} return null;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result,
			HttpServletResponse httpServletResponse, ModelAndView model, HttpServletRequest request){
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
		
		//Restful entre maquinas: busqueda del usuario en el servicio para comprobar si existe
		RestTemplate restTemplate = new RestTemplate();
		String urlRequest = request.getRequestURL().toString().replace
				(request.getRequestURI(),"/user/" + signupForm.getUsername());
		logger.info(urlRequest);
		User user = restTemplate.getForObject(urlRequest, User.class);
		if(user!=null){
			logger.info("usuario creado: " + user.getUsername());
			httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
			return new ModelAndView("login");
		}
        logger.info("User " + signupForm.getUsername() + " not created");
		httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return model;
	}	
}