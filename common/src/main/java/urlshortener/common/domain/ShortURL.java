package urlshortener.common.domain;

import java.net.URI;
import java.sql.Date;

public class ShortURL {

	private String hash;
	private String target;
	private URI uri;
	private String sponsor;
	private Date created;
	private String owner;
	private Integer mode;
	private Boolean safe;
	private String ip;
	private String country;
	private String status;
	private Date lastStatus;
	private String username;

	public ShortURL(String hash, String target, URI uri, String sponsor,
			Date created, String owner, Integer mode, Boolean safe, String ip,
			String country, String status, Date lastStatus, String username) {
		this.hash = hash;
		this.target = target;
		this.uri = uri;
		this.sponsor = sponsor;
		this.created = created;
		this.owner = owner;
		this.mode = mode;
		this.safe = safe;
		this.ip = ip;
		this.country = country;
		this.status = status;
		this.lastStatus = lastStatus;
		this.username = username;
	}

	public ShortURL() {
	}

	public String getHash() {
		return hash;
	}

	public String getTarget() {
		return target;
	}

	public URI getUri() {
		return uri;
	}

	public Date getCreated() {
		return created;
	}

	public String getOwner() {
		return owner;
	}

	public Integer getMode() {
		return mode;
	}

	public String getSponsor() {
		return sponsor;
	}

	public Boolean getSafe() {
		return safe;
	}

	public String getIP() {
		return ip;
	}

	public String getCountry() {
		return country;
	}

	public String getStatus() {
		return status;
	}

	public Date getLastStatus() {
		return lastStatus;
	}
	
	public String getUsername() {
		return username;
	}

	public void setTotalStatus(String status, Date lastStatus) {
		this.status = status;
		this.lastStatus = lastStatus;
	}
}
