package urlshortener.grupo6.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.domain.Statistic;
import urlshortener.common.domain.User;
import urlshortener.common.repository.UserRepository;
import urlshortener.common.web.UrlShortenerController;
import urlshortener.grupo6.security.SignupForm;
import urlshortener.grupo6.security.UserDetailsImpl;
import urlshortener.grupo6.security.UserService;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

	@Override
	@RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);
		return super.redirectTo(id, request);
	}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		logger.info("Requested new short for uri " + url);
		return super.shortener(url, sponsor, request);
	}

	@Override
	@RequestMapping(value = "/{id}+", method = RequestMethod.GET)
	public ResponseEntity<Statistic> showStatistic(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested statistic for uri with id " + id);
		return super.showStatistic(id,request);
	}

	@Override
	@RequestMapping(value = "/uploadUrl", method = RequestMethod.POST)
	public ResponseEntity<?> uploadUrl(MultipartHttpServletRequest request) {
		logger.info("Requested uploadUrl");
		return super.uploadUrl(request);
	}
}