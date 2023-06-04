package tg.news.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.news.dto.NewsDto;
import tg.news.service.FeedFetcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class FeedManagerImplTest {

    @Mock
    FeedFetcher feedFetcher;
    @InjectMocks
    FeedManagerImpl feedManager;

    @Test
    void fetchNewsFromRss_shouldReturnEmptyList_whenSyndFeedIsNull() {
        Mockito.when(feedFetcher.fetchFeed(anyString())).thenReturn(null);
        List<NewsDto> news = feedManager.fetchNewsFromRss(anyString());

        assertNotNull(news);
        assertEquals(0, news.size());
    }

}