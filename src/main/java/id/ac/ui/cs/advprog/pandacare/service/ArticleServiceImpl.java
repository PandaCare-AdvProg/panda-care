package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import id.ac.ui.cs.advprog.pandacare.repository.ArticleRepository;
import id.ac.ui.cs.advprog.pandacare.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repo;

    @Autowired
    public ArticleServiceImpl(ArticleRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Article> getAllPublishedArticles() {
        return repo.findByPublishedTrueOrderByPublishedAtDesc();
    }

    @Override
    public List<Article> getPublishedArticlesByCategory(String category) {
        return repo.findByPublishedTrueAndCategoryIgnoreCaseOrderByPublishedAtDesc(category);
    }
}
