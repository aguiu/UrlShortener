package urlshortener.grupo6.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import urlshortener.common.repository.UserRepository;
import urlshortener.common.security.CustomBasicAuthenticationEntryPoint;
import urlshortener.common.security.UserServiceImpl;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;
	
	private static String REALM = "URLSHORTENER";
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        	.authorizeRequests()
        	.antMatchers("/user/**").hasRole("USER")
        	.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint());
        http
        	.formLogin()
        	.loginPage("/login")
        	.loginProcessingUrl("/login")
        	.usernameParameter("username")
        	.passwordParameter("password")
        	.defaultSuccessUrl("/")
        	.permitAll()
        	.and().logout()
        	.logoutUrl("/logout")
        	.logoutSuccessUrl("/")
        	.permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserServiceImpl(userRepository));
        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
    }
    
    @Override
    public UserDetailsService userDetailsServiceBean() {
        return new UserServiceImpl(userRepository);
    }
    
	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
