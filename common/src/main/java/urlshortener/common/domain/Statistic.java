package urlshortener.common.domain;

import java.sql.Date;

public class Statistic {

	private String url;
	private Date created;
	private Long clicks;

	public Statistic(String url, Date created, Long clicks) {
		this.url = url;
		this.created = created;
		this.clicks = clicks;
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
}
