package urlshortener.grupo6.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import urlshortener.common.domain.ShortURL;
import urlshortener.common.repository.ShortURLRepository;
import urlshortener.common.repository.UserRepository;

@RestController
public class Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	protected ShortURLRepository shortURLRepository;
	
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
	
	@RequestMapping("/advert/{id}")
    public ModelAndView greeting(@PathVariable String id, Model model, HttpServletRequest request) {
		ShortURL su = shortURLRepository.findByKey(id);
        model.addAttribute("name", su.getTarget());
        logger.info("advert:   "+su.getTarget());
        return new ModelAndView("advert");
    }
}
