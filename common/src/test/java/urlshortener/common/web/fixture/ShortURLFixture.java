package urlshortener.common.web.fixture;

import urlshortener.common.domain.ShortURL;

public class ShortURLFixture {

	public static ShortURL someUrl() {
		return new ShortURL("someKey", "http://example.com/", null, false, null,
				null, 307, true, null, null);
	}
}
