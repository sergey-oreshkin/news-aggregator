package tg.news.service.impl;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.news.dto.NewsDto;
import tg.news.service.FeedFetcher;
import tg.news.service.FeedManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedManagerImpl implements FeedManager {

    private final FeedFetcher feedFetcher;

    @Override
    public List<NewsDto> fetchNewsFromRss(String url) {
        SyndFeed syndFeed = feedFetcher.fetchFeed(url);
        if (syndFeed == null) return new ArrayList<>();
        return mapEntriesToNewsDto(syndFeed.getEntries());
    }

    private List<NewsDto> mapEntriesToNewsDto(List<SyndEntry> entries) {
        return entries.stream()
                .map(entry -> NewsDto.builder()
                        .title(entry.getTitle().trim().replaceAll("\n|&.{0,};|<.{0,}>", ""))
                        .description(
                                entry.getDescription() == null
                                        ?
                                        ""
                                        :
                                        entry.getDescription().getValue().trim().replaceAll("\n|&.{0,};|<.{0,}>", "")
                        )
                        .link(entry.getLink().trim())
                        .date(entry.getPublishedDate() == null ? new Date() : entry.getPublishedDate())
                        .build()
                ).collect(Collectors.toList());
    }
}
