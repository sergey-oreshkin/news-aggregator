package tg.news.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Builder
@Document(indexName = "feed")
public class News {
    @Id
    private String link;
    private String title;
    private String description;
    private Date date;
}
