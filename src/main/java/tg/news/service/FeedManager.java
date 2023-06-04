package tg.news.service;

import tg.news.dto.NewsDto;

import java.util.List;

public interface FeedManager {
    List<NewsDto> fetchNewsFromRss(String url);
}
