package urlshortener.common.repository;

import urlshortener.common.domain.User;

public interface UserRepository {
	
	User findByUsername(String username);
	
	User register(User user);
	
	void modify(User user);
	
	void delete(String username);
}
