package urlshortener.common.domain;

import java.sql.Date;
import java.util.List;
import urlshortener.common.domain.Par;

public class Statistic {

	private String url, ip;
	private Date created;
	private Long clicks;
	private List<Par> ipVisitantes;

	public Statistic(String url, Date created, Long clicks, String ip, List<Par> ipVisitantes) {
		this.url = url;
		this.created = created;
		this.clicks = clicks;
		this.ip = ip;
		this.ipVisitantes = ipVisitantes;
	}

	public Long getClicks() {
		return clicks;
	}

	public String getUrl() {
		return url;
	}

	public Date getCreated() {
		return created;
	}

	public String getIp() {
		return ip;
	}

	public List<Par> getVisitantes() {
		return ipVisitantes;
	}
}
