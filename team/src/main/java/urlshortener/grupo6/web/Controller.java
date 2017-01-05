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
import urlshortener.grupo6.security.SignupForm;
import urlshortener.common.domain.Statistic;

import urlshortener.common.repository.ClickRepository;
import urlshortener.common.domain.Par;
import java.util.List;
import java.util.ArrayList;
import urlshortener.common.domain.Click;

@RestController
public class Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	protected ShortURLRepository shortURLRepository;
	
	@Autowired
	protected UserRepository userRespository;

	@Autowired
	protected ClickRepository clickRepository;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request){
		logger.info("INICIO");
		return new ModelAndView("index");		
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public ModelAndView register(ModelAndView model){
		model.addObject("signupForm", new SignupForm());
		return model;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request){
		return new ModelAndView("login");		
	}
	
	
	@RequestMapping("/advert/{id}")
    public ModelAndView advert(@PathVariable String id, Model model, HttpServletRequest request) {
        logger.info("advert:   "+id);
        return new ModelAndView("advert");
    }
	
	@RequestMapping("/uri/{id}")
    public ShortURL targetUri(@PathVariable String id, HttpServletRequest request) {
		ShortURL su = shortURLRepository.findByKey(id);
		String s = su.getTarget();
        logger.info(" geting target uri :   "+s);
        return su;
    }
	

    @RequestMapping("/404error/{id}")
    public ModelAndView errorOffline(@PathVariable String id, Model model, HttpServletRequest request) {
    	// Obtenemos ya los datos de la URL para mostrarlos en el HTML
    	ShortURL su = shortURLRepository.findByKey(id);
    	model.addAttribute("lastStatus", su.getLastStatus());
    	model.addAttribute("uri", su.getTarget());
    	logger.info("Entramos en reedireccion a 404error.html");
    	return new ModelAndView("404error");
    }

    @RequestMapping("/stats/{id}")
    public ModelAndView statsHtml(@PathVariable String id, Model model, HttpServletRequest request) {
    	logger.info("Entramos en la redirección a stats.html");
		ShortURL su = shortURLRepository.findByKey(id);
		Long numberOfRedirect = (long) 0;
		numberOfRedirect = clickRepository.clicksByHash(id);
		if (su != null) {
			List<Click> visitantes = clickRepository.visitantes(id);
			List<Par> visitasPorIp = visitasPorIp(visitantes);
			Statistic statistic = new Statistic(su.getTarget(),su.getCreated(),numberOfRedirect,
				su.getIP(),visitasPorIp);
	    	String tablaVisitas = "<table class=\"table\"><tr><td><strong>Usuario</strong>"
	    			+ "</td><td><strong>Visitas totales</strong></td></tr>";
	    	for(int i = 0; i<visitasPorIp.size(); i++) {
	    		String usuario = visitasPorIp.get(i).getIp();
	    		int numVisitas = visitasPorIp.get(i).getNumVeces();
	    		tablaVisitas = tablaVisitas + "<tr><td>" + usuario + "</td><td>" + numVisitas + "</td></tr>";
	    	}
	    	tablaVisitas = tablaVisitas + "</table>";
	    	model.addAttribute("created", statistic.getCreated());
	    	model.addAttribute("ip", statistic.getIp());
	    	model.addAttribute("uri", statistic.getUrl());
	    	model.addAttribute("html", tablaVisitas);
	    }
    	return new ModelAndView("stats");
    }

    private List<Par> visitasPorIp(List<Click> visitas) {
		List<String> yaComprobados = new ArrayList<String>();
		List<Par> visitasIp = new ArrayList<Par>();
		for (int i = 0; i<visitas.size(); i++) {
			Par par = new Par();
			int numeroDeVeces = 0;
			String ip = visitas.get(i).getIp();
			if (!yaComprobados.contains(ip)) {
				yaComprobados.add(ip);
				for (int j = 0; j<visitas.size(); j++) {
					if (ip.equals(visitas.get(j).getIp())) {
						numeroDeVeces++;
					}
				}
				par.setIp(ip);
				par.setNumVeces(numeroDeVeces);
				visitasIp.add(par);
			}
		}
		return visitasIp;
	}
}
