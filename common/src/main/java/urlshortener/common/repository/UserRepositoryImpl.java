package urlshortener.common.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import urlshortener.common.domain.User;

@Repository
public class UserRepositoryImpl implements UserRepository{

	private static final Logger log = LoggerFactory
			.getLogger(UserRepositoryImpl.class);

	private static final RowMapper<User> rowMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getString("username"), rs.getString("password"),
					rs.getString("email"));
		}
	};
	
	@Autowired
	protected JdbcTemplate jdbc;
	
	public UserRepositoryImpl(JdbcTemplate jdbc){
		this.jdbc = jdbc;
	}
	
	@Override
	public User findByUsername(String username) {
		try {
			log.info("Buscando: " + username);
			return jdbc.queryForObject("SELECT * FROM user WHERE username=?",
					rowMapper, username);
		} catch (Exception e) {
			log.info("When select for username " + username, e);
			return null;
		}
	}

	@Override
	public User register(User user) {
		try {
			jdbc.update("INSERT INTO user VALUES (?,?,?)",
					user.getUsername(),user.getPassword(),user.getEmail());
		} catch (DuplicateKeyException e) {
			log.debug("When insert for username " + user.getUsername(), e);
			return user;
		} catch (Exception e) {
			log.debug("When insert", e);
			return null;
		}
		log.info("DATOS EN USER: " + Long.toString(jdbc.queryForObject("select count(*) from user", Long.class)));
		return user;
	}

	@Override
	public void modify(User user) {
		log.info("new password: "+user.getPassword()+" new Email: "+user.getEmail());
		try {
			jdbc.update(
					"update user set password=?, email=?",
					user.getPassword(),user.getEmail());			
		} catch (Exception e) {
			log.info("When update for username " + user.getUsername(), e);
		}		
	}

	@Override
	public void delete(String username) {
		try {
			jdbc.update("delete from user where username=?", username);
		} catch (Exception e) {
			log.debug("When delete for username " + username, e);
		}
	}
}
