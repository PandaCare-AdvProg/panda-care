package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import java.util.List;

public interface ArticleService {
    List<Article> getAllPublishedArticles();
    List<Article> getPublishedArticlesByCategory(String category);
}
