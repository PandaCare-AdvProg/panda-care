package id.ac.ui.cs.advprog.pandacare.service;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    private ArticleRepository articleRepository;
    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        articleService = new ArticleServiceImpl(articleRepository);
    }

    @Test
    void testGetAllPublishedArticles() {
        Doctor doctor = new Doctor();
        List<Article> mockArticles = Arrays.asList(
                Article.builder().title("A1").content("C1").category("Health").publishedAt(LocalDateTime.now()).published(true).author(doctor).build(),
                Article.builder().title("A2").content("C2").category("Health").publishedAt(LocalDateTime.now()).published(true).author(doctor).build()
        );
        when(articleRepository.findByPublishedTrueOrderByPublishedAtDesc()).thenReturn(mockArticles);

        List<Article> result = articleService.getAllPublishedArticles();

        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findByPublishedTrueOrderByPublishedAtDesc();
    }

    @Test
    void testGetPublishedArticlesByCategory() {
        Doctor doctor = new Doctor();
        String category = "Health";
        List<Article> mockArticles = Arrays.asList(
                Article.builder().title("A1").content("C1").category(category).publishedAt(LocalDateTime.now()).published(true).author(doctor).build()
        );
        when(articleRepository.findByPublishedTrueAndCategoryIgnoreCaseOrderByPublishedAtDesc(category)).thenReturn(mockArticles);

        List<Article> result = articleService.getPublishedArticlesByCategory(category);

        assertEquals(1, result.size());
        verify(articleRepository, times(1)).findByPublishedTrueAndCategoryIgnoreCaseOrderByPublishedAtDesc(category);
    }
}