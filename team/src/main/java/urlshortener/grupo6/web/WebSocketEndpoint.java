package urlshortener.grupo6.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import urlshortener.common.domain.Par;
import java.lang.Thread;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import urlshortener.common.repository.ShortURLRepository;

@Controller
public class WebSocketEndpoint{
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    
    @Autowired
	protected ShortURLRepository shortURLRepository;

    @MessageMapping("/topic/answer")
    @SendTo("/topic/answer")
    public Integer answeringToWebSocket(){
    	List<String> allUrl = shortURLRepository.allList();
    	logger.info("accedido al ws endpoint!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	//Par par = new Par("prueba", 10);
    	Integer num =allUrl.size() ;
    	return num;
    }

}