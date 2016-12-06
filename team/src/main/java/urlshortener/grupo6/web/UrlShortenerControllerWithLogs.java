package urlshortener.grupo6.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import urlshortener.common.domain.ShortURL;
import urlshortener.common.domain.Statistic;
import urlshortener.common.web.UrlShortenerController;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

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
	@RequestMapping(value = "/p/{id:(?!link|publi).*}", method = RequestMethod.GET)
	public String redirectToPubli(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection  to Publi " + id);
		return super.redirectToPubli(id, request);
	}

	@Override
	@RequestMapping(value = "/uploadUrl", method = RequestMethod.POST)
	public ResponseEntity<?> uploadUrl(MultipartHttpServletRequest request) {
		logger.info("Requested uploadUrl");
		return super.uploadUrl(request);
	}
	
}
