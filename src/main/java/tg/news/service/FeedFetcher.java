package tg.news.service;

import com.sun.syndication.feed.synd.SyndFeed;

public interface FeedFetcher {
    SyndFeed fetchFeed(String url);
}
