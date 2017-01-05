package urlshortener.grupo6.web;

import java.sql.Date;

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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;

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

@EnableScheduling
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
		ResponseEntity<ShortURL> shortener = super.shortener(url, sponsor, request);
		HttpStatus status = shortener.getStatusCode();
		if (status.equals(HttpStatus.CREATED)) {
					logger.info("URI was CREATED");
					ShortURL su = shortener.getBody();
					su.setTotalStatus("online", new Date(System.currentTimeMillis()));
		}
		return shortener;
	}

	@Override
	@RequestMapping(value = "/{id}+", method = RequestMethod.GET)
	public ResponseEntity<Statistic> showStatistic(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested statistic for uri with id " + id);
		return super.showStatistic(id,request);
	}

	@Override
	@RequestMapping(value = "/{id}+html", method = RequestMethod.GET)
	public ResponseEntity<String> showStatisticHtml(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested statistic for uri with id " + id);
		return super.showStatisticHtml(id,request);
	}

	@Override
	@RequestMapping(value = "/uploadUrl", method = RequestMethod.POST)
	public ResponseEntity<?> uploadUrl(MultipartHttpServletRequest request) {
		logger.info("Requested uploadUrl");
		return super.uploadUrl(request);
	}
}