package tg.news.service.impl;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tg.news.service.FeedFetcher;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Service
@Slf4j
public class FeedFetcherImpl implements FeedFetcher {
    private static final int timeout = 1000;

    @Override
    public SyndFeed fetchFeed(String url) {
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            XmlReader reader = new XmlReader(conn);
            return new SyndFeedInput().build(reader);
        } catch (IOException e) {
            log.warn("Failed to connect - " + url + " skipped");
            return null;
        } catch (FeedException | NullPointerException e) {
            log.warn("Failed to parse response from - " + url + " skipped");
            return null;
        }
    }
}
