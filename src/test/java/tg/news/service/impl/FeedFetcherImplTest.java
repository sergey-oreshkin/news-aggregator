package tg.news.service.impl;

import com.sun.syndication.feed.synd.SyndFeed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class FeedFetcherImplTest {
    public static final String INVALID_URL = "http://";

    FeedFetcherImpl feedFetcher = new FeedFetcherImpl();


    @Test
    void fetchFeed_shouldReturnNull_whenExceptionDuringRequest() {
        SyndFeed syndFeed = feedFetcher.fetchFeed(INVALID_URL);

        Assertions.assertNull(syndFeed);
    }
}