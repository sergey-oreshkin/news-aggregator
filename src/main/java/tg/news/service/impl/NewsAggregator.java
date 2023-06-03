package tg.news.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tg.news.dto.NewsDto;
import tg.news.model.News;
import tg.news.model.Rss;
import tg.news.repository.NewsElasticRepository;
import tg.news.repository.RssJpaRepository;
import tg.news.service.FeedManager;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsAggregator {
    private final RssJpaRepository rssRepository;
    private final FeedManager feedManager;
    private final NewsElasticRepository newsElacticRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Scheduled(fixedRateString = "${app.request.period}", initialDelayString = "${app.request.delay}")
    public void refreshNews() {
        System.out.println(newsElacticRepository.count());
        log.info("Start fetching news");
        List<Rss> rss = rssRepository.findAll();
        List<News> news = rss.stream()
                .map(Rss::getSource)
                .map(feedManager::fetchNewsFromRss)
                .flatMap(Collection::stream)
                .map(this::map)
                .toList();
        log.debug("News fetched");
        log.debug("Try to save news");
        newsElacticRepository.saveAll(news);
        log.info("News refreshed. Total news count = {}", newsElacticRepository.count());
    }

    private News map(NewsDto news) {
        return News.builder()
                .title(news.getTitle())
                .description(news.getDescription())
                .date(news.getDate())
                .link(news.getLink())
                .build();
    }
}
