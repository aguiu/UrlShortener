package urlshortener.grupo6.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import urlshortener.common.domain.Par;
import java.lang.Thread;

@Controller
public class WebSocketEndpoint{
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    @MessageMapping("/conected")
    @SendTo("/topic/answer")
    public Integer answeringToWebSocket(){
    	logger.info("accedido al ws endpoint!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	//Par par = new Par("prueba", 10);
    	Integer num = new Integer(10);
    	return num;
    }

}