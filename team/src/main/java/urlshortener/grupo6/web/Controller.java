package urlshortener.grupo6.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import urlshortener.common.repository.UserRepository;

@RestController
public class Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	protected UserRepository userRespository;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request){
		logger.info("INICIO");
		return new ModelAndView("index");		
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest request){
		return new ModelAndView("register");		
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request){
		return new ModelAndView("login");		
	}
}
