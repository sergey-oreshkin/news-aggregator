package tg.news.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tg.news.model.News;

public interface NewsElasticRepository extends ElasticsearchRepository<News, String> {
}
