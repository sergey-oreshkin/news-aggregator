package tg.news;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tg.news.model.News;
import tg.news.model.Rss;
import tg.news.repository.NewsElasticRepository;
import tg.news.repository.RssJpaRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class AppTest {

    static final String TITLE = "title one";

    static final String LINK = "http://example.com/example1";

    static final String DESCRIPTION = "description example 1 test";

    static final String responseXml = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<rss version=\"2.0\">\n" +
                    "    <channel>\n" +
                    "        <item>\n" +
                    "            <author>example 1</author>\n" +
                    "            <title>%s</title>\n" +
                    "            <link>%s</link>\n" +
                    "            <description>\n" +
                    "                <![CDATA[%s]]>\n" +
                    "            </description>\n" +
                    "            <pubDate>%s</pubDate>\n" +
                    "        </item>\n" +
                    "        <item>\n" +
                    "            <author>example 1</author>\n" +
                    "            <title>%s</title>\n" +
                    "            <link>%s</link>\n" +
                    "        </item>\n" +
                    "    </channel>\n" +
                    "</rss>",
            TITLE, LINK, DESCRIPTION, new Date(), TITLE, LINK);

    static MockWebServer mockWebServer;
    static HttpUrl mockedUrl;

    @MockBean
    NewsElasticRepository newsElasticRepository;
    @MockBean
    RssJpaRepository rssJpaRepository;
    @Captor
    ArgumentCaptor<List<News>> newsCaptor;

    @BeforeAll
    static void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockedUrl = mockWebServer.url("/");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseXml)
                .addHeader("Content-Type", "text/xml; charset=UTF-8"));
    }

    @Test
    void scheduledMethod_shouldSaveNewsToElastic_whenReceiveItFromRss() throws InterruptedException {
        when(rssJpaRepository.findAll()).thenReturn(List.of(Rss.builder().source(mockedUrl.toString()).build()));

        Thread.sleep(1000);

        verify(newsElasticRepository).saveAll(newsCaptor.capture());
        List<News> news = newsCaptor.getValue();

        assertNotNull(news);
        assertEquals(2, news.size());
        assertEquals(TITLE, news.get(0).getTitle());
        assertEquals(DESCRIPTION, news.get(0).getDescription());
        assertEquals(LINK, news.get(0).getLink());
    }
}
