package id.ac.ui.cs.advprog.pandacare.controller;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import id.ac.ui.cs.advprog.pandacare.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleControllerTest {

    private ArticleService articleService;
    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        articleService = mock(ArticleService.class);
        articleController = new ArticleController(articleService);
    }

    @Test
    void testListPublishedWithoutCategory() {
        List<Article> mockArticles = Arrays.asList(new Article(), new Article());
        when(articleService.getAllPublishedArticles()).thenReturn(mockArticles);

        ResponseEntity<List<Article>> response = articleController.listPublished(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockArticles, response.getBody());
        verify(articleService, times(1)).getAllPublishedArticles();
        verify(articleService, never()).getPublishedArticlesByCategory(anyString());
    }

    @Test
    void testListPublishedWithCategory() {
        String category = "Health";
        List<Article> mockArticles = Arrays.asList(new Article());
        when(articleService.getPublishedArticlesByCategory(category)).thenReturn(mockArticles);

        ResponseEntity<List<Article>> response = articleController.listPublished(category);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockArticles, response.getBody());
        verify(articleService, times(1)).getPublishedArticlesByCategory(category);
        verify(articleService, never()).getAllPublishedArticles();
    }

    @Test
    void testListPublishedWithBlankCategory() {
        List<Article> mockArticles = Arrays.asList(new Article());
        when(articleService.getAllPublishedArticles()).thenReturn(mockArticles);

        ResponseEntity<List<Article>> response = articleController.listPublished("   ");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockArticles, response.getBody());
        verify(articleService, times(1)).getAllPublishedArticles();
        verify(articleService, never()).getPublishedArticlesByCategory(anyString());
    }
}
