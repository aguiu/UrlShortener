package urlshortener.grupo6.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import urlshortener.common.repository.ClickRepository;
import urlshortener.common.repository.ClickRepositoryImpl;
import urlshortener.common.repository.ShortURLRepository;
import urlshortener.common.repository.ShortURLRepositoryImpl;
import urlshortener.common.repository.UserRepository;
import urlshortener.common.repository.UserRepositoryImpl;
import urlshortener.common.security.UserService;
import urlshortener.common.security.UserServiceImpl;

@Configuration
public class PersistenceContext {

	@Autowired
    protected JdbcTemplate jdbc;

	@Bean
	ShortURLRepository shortURLRepository() {
		return new ShortURLRepositoryImpl(jdbc);
	}
 	
	@Bean
	ClickRepository clickRepository() {
		return new ClickRepositoryImpl(jdbc);
	}
	
	@Bean
	UserRepository userRepository(){
		return new UserRepositoryImpl(jdbc);
	}
	
	@Bean
	UserService userService(){
		return new UserServiceImpl(userRepository());
	}
}