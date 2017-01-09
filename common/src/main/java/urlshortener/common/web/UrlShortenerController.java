package urlshortener.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.repository.ClickRepository;
import urlshortener.common.repository.ShortURLRepository;
import urlshortener.common.domain.Click;
import urlshortener.common.domain.Statistic;
import urlshortener.common.domain.Par;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UrlShortenerController {
	private static final Logger LOG = LoggerFactory
			.getLogger(UrlShortenerController.class);
	@Autowired
	protected ShortURLRepository shortURLRepository;

	@Autowired
	protected ClickRepository clickRepository;

	@RequestMapping(value = "/{id:(?!link).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id,
			HttpServletRequest request) {
		ShortURL l = shortURLRepository.findByKey(id);
		LOG.info("Entrado en redirectTo");
		if (l != null && l.getStatus().equals("online")) {
			createAndSaveClick(id, extractIP(request));
			return createSuccessfulRedirectToResponse(l);
		} else if(l != null && l.getStatus().equals("offline")) {
			LOG.info("SERVICIO NO DISPONIBLE");
			return create404RedirectToResponse(l);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private void createAndSaveClick(String hash, String ip) {
		Click cl = new Click(null, hash, new Date(System.currentTimeMillis()),
				null, null, null, ip, null);
		cl=clickRepository.save(cl);
		LOG.info(cl!=null?"["+hash+"] saved with id ["+cl.getId()+"]":"["+hash+"] was not saved");
	}

	private String extractIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
		HttpHeaders h = new HttpHeaders();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		if (l != null){
			if(l.getSponsor()!=null && !name.equals(l.getUsername())){
				LOG.info("Entrando en createSuccessfulRedirectToResponse");
				LOG.info("Enlace con publicidad: redireccionando a anuncio... "+l.getTarget());
				h.setLocation(URI.create("http://localhost:8080/advert/"+l.getHash()));
				return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
			}
			else {
				h.setLocation(URI.create(l.getTarget()));
				return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
			}
		}
		else{
			LOG.error("url no valida");
			return new ResponseEntity <> (HttpStatus.BAD_REQUEST);
		}	
	}

	private ResponseEntity<?> create404RedirectToResponse(ShortURL l) {
		HttpHeaders h = new HttpHeaders();
		if (l != null){
			h.setLocation(URI.create("http://localhost:8080/404error/" + l.getHash()));
			return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
		}
		else{
			LOG.error("url no valida");
			return new ResponseEntity <> (HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/link", method = RequestMethod.POST)
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		LOG.info("Entrando en shortener");
		UrlValidator urlValidator = new UrlValidator(new String[] { "http",
		"https" });
		LOG.info("sponsor: " + sponsor);
		if (urlValidator.isValid(url)) {
			if(isOnline(url)){
				ShortURL su = createAndSaveIfValid(url, sponsor, 
						UUID.randomUUID().toString(), extractIP(request));
				if (su != null) {
					HttpHeaders h = new HttpHeaders();
					h.setLocation(su.getUri());
					return new ResponseEntity<>(su, h, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			} else{
				LOG.error("url no alcanzable");
				return new ResponseEntity <> (HttpStatus.SERVICE_UNAVAILABLE);
			}
		} else{
			LOG.error("url no valida");
			return new ResponseEntity <> (HttpStatus.BAD_REQUEST);
		}
	}

	private ShortURL createAndSaveIfValid(String url, String sponsor,
										  String owner, String ip) {
			LOG.info("Entrando en createAndSaveIfValid");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String name = auth.getName();
			LOG.info("Usuario identificado: " + name);
			String id = Hashing.murmur3_32()
					.hashString(url, StandardCharsets.UTF_8).toString();
			ShortURL su = new ShortURL(id, url,
					linkTo(
							methodOn(UrlShortenerController.class).redirectTo(
									id, null)).toUri(), sponsor, new Date(
							System.currentTimeMillis()), owner,
					HttpStatus.TEMPORARY_REDIRECT.value(), true, ip, null,
					"online", new Date(System.currentTimeMillis()),name);
			return shortURLRepository.save(su);
	}
	
	/**
	 * Cierto si [url] es accesible
	 */
	private boolean isOnline(String url){
		try{
			LOG.info("Entrando en isOnline");
			HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
			c.setRequestMethod("HEAD");
			return c.getResponseCode() == 200;
		} catch (IOException e){
			return false;
		}
	}

	@RequestMapping(value = "/{id}+", method = RequestMethod.GET)
	public ResponseEntity<Statistic> showStatistic(String id, HttpServletRequest request) {
		LOG.info("Entrando showStatistics");
		ShortURL su = shortURLRepository.findByKey(id);
		Long numberOfRedirect = (long) 0;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		if (su != null && name.equals(su.getUsername())) {
			HttpHeaders h = new HttpHeaders();
			try {
				h.setLocation(new URI("http://http://localhost:8080/" + su.getHash()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			numberOfRedirect = clickRepository.clicksByHash(id);
			List<Click> visitantes = clickRepository.visitantes(id);
			List<Par> visitasPorIp = visitasPorIp(visitantes);
			if (visitasPorIp == null) {
				LOG.info("LIST VISITANTES ES NULO");
			} else {
				for (int i = 0; i<visitasPorIp.size(); i++) {
					LOG.info("Visitante nº" + i + " es " + visitasPorIp.get(i).getIp() + " con "
						+ visitasPorIp.get(i).getNumVeces());
				} 
			}
			LOG.info("Numero de veces que se ha utilizado " + id + " = " + numberOfRedirect);
			LOG.info("Fecha de creación de " + id + " = " + su.getCreated());
			LOG.info("URL destino de " + id + " = " + su.getTarget());
			Statistic statistic = new Statistic(su.getTarget(),su.getCreated(),numberOfRedirect,
				su.getIP(),visitasPorIp);
			return new ResponseEntity<>(statistic, h, HttpStatus.OK);
		} else {
			LOG.info("NO ENCONTRADO");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{id}+html", method = RequestMethod.GET)
	public ResponseEntity<String> showStatisticHtml(String id, HttpServletRequest request) {
		HttpHeaders h = new HttpHeaders();
		ShortURL l = shortURLRepository.findByKey(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		if (l!=null && name.equals(l.getUsername())) {
			LOG.info("Entramos correctamente a showStatisticHtml");
			String url = "http://localhost:8080/stats/" + id;
			LOG.info("Devolvemos " + url);
			return new ResponseEntity<>(url, h, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/uploadUrl", method = RequestMethod.POST)
    public ResponseEntity<?> uploadUrl(MultipartHttpServletRequest request) { 
		String respuesta = "";
        Iterator<String> itrator = request.getFileNames();
        MultipartFile multiFile = request.getFile(itrator.next());
        if(multiFile != null) {
	        try {
	            // just to show that we have actually received the file
	            System.out.println("File Length:" + multiFile.getBytes().length);
	            System.out.println("File Type:" + multiFile.getContentType());
	            String fileName=multiFile.getOriginalFilename();
	            System.out.println("File Name:" +fileName);
	
	            //making directories for our required path.
	            File destFile = new File(fileName);
	            byte[] bytes = multiFile.getBytes();
	            BufferedOutputStream stream = new BufferedOutputStream(
	                    new FileOutputStream(destFile));
	            stream.write(bytes);
	            stream.close();
	            respuesta = acortarURIs(fileName,request);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        } else {
        	LOG.info("NO ENCONTRADO");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(toJson(respuesta), HttpStatus.CREATED);
    }
	
	private String toJson(Object data) {
        ObjectMapper mapper=new ObjectMapper();
        StringBuilder builder=new StringBuilder();
        try {
            builder.append(mapper.writeValueAsString(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
	
	private String acortarURIs(String fileName, 
			MultipartHttpServletRequest request) throws IOException {
		String cadena, respuesta = "";
	    FileReader f = new FileReader(fileName);
	    BufferedReader b = new BufferedReader(f);
	    cadena = b.readLine();
	    if(sonValidas(fileName)) {
	    	 while (cadena != null) {
	 	    	ResponseEntity<ShortURL> re = shortener(cadena,"sponsor",request);
	 	    	respuesta = respuesta + ";" + re;
	 	    	cadena = b.readLine();
	 	    }
	    }
	    b.close();
	    System.out.println(respuesta);
		return respuesta;
	}
	
	/**
	 * Comprueba que todas las URIs que se han pasado en el fichero
	 * CSV sean válidas. Devuelve false en caso contrario.
	 */
	private boolean sonValidas(String fileName) throws IOException {
		String cadena;
	    FileReader f = new FileReader(fileName);
	    BufferedReader b = new BufferedReader(f);
	    cadena = b.readLine();
		while (cadena != null) {
	    	if(!isOnline(cadena)) {
	    		b.close();
	    		return false;
	    	}
	    	cadena = b.readLine();
	    }
		b.close();
		return true;
	}

	@Async
	@Scheduled(fixedRate=10000)
	private void updateStatus() {
		List<String> allUrl = shortURLRepository.allList();
		if (allUrl == null) {
			LOG.info("Lista nula");
		} else {
			LOG.info("Tamaño de la lista = " + allUrl.size());
			for (String url : allUrl) {
				if(isOnline(url)) {
					// Actualiza todas las URLs con el nuevo 
					// tiempo e indicando que esta online
					shortURLRepository.updateAllOnline(url);
				} else {
					// Añadir valor no esta online es la BD
					// para que redireccione a error 404
					shortURLRepository.updateAllOffline(url);
				}
				LOG.info("ShortURL " + url + 
					" se ha actualizado con nueva disponibilidad = " 
					+ isOnline(url));
			}
		}
		LOG.info("FIN updateUriStatus");
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