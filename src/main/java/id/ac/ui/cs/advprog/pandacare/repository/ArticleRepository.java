package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublishedTrueOrderByPublishedAtDesc();
    List<Article> findByPublishedTrueAndCategoryIgnoreCaseOrderByPublishedAtDesc(String category);
}
