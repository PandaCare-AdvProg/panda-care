package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import id.ac.ui.cs.advprog.pandacare.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService service;

    @Autowired
    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Article>> listPublished(
            @RequestParam(value = "category", required = false) String category
    ) {
        List<Article> articles = (category != null && !category.isBlank())
                ? service.getPublishedArticlesByCategory(category.trim())
                : service.getAllPublishedArticles();

        return ResponseEntity.ok(articles);
    }
}
