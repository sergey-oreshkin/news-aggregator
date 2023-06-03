package tg.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.news.model.Rss;

public interface RssJpaRepository extends JpaRepository<Rss, Integer> {
}
