package tg.news.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class NewsDto {
    private String title;
    private String description;
    private Date date;
    private String link;
}
