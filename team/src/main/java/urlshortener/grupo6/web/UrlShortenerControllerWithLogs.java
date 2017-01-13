package urlshortener.grupo6.web;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.http.HttpServletRequest;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.domain.Statistic;
import urlshortener.common.repository.ShortURLRepository;
import urlshortener.common.repository.UserRepository;
import urlshortener.common.security.UserService;
import urlshortener.common.web.UrlShortenerController;

@EnableScheduling
@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected ShortURLRepository shortURLRepository;
	
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
	public ResponseEntity<?> showStatistic(@PathVariable String id, HttpServletRequest request) {
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