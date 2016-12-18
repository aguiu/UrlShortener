package urlshortener.grupo6.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import urlshortener.common.domain.User;
import urlshortener.common.repository.UserRepository;

@Service
@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public class UserServiceImpl implements UserService, UserDetailsService{
	
	private static final Logger log = LoggerFactory
			.getLogger(UserServiceImpl.class);
	
	@Autowired
	protected UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException{
		User user = userRepository.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException(username);
		log.info("Usuario cargado: " + user.getUsername());
		return new UserDetailsImpl(user);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void signup(SignupForm signupForm){
		User user = new User(signupForm.getUsername(),
				signupForm.getPassword(), signupForm.getEmail());
		log.info("signup " + user.getEmail());
		userRepository.register(user);		
	}

	@Override
	public boolean isUserExist(SignupForm signupForm) {
		User aux = userRepository.findByUsername(signupForm.getUsername());		
		return aux!=null;
	}
}
