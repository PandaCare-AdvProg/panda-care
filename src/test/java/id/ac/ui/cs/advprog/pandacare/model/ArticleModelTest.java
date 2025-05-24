package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ArticleModelTest {

    @Test
    void testArticleBuilderAndGetters() {
        Doctor doctor = new Doctor();
        LocalDateTime now = LocalDateTime.now();

        Article article = Article.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .category("Health")
                .publishedAt(now)
                .published(true)
                .author(doctor)
                .build();

        assertEquals(1L, article.getId());
        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Content", article.getContent());
        assertEquals("Health", article.getCategory());
        assertEquals(now, article.getPublishedAt());
        assertTrue(article.isPublished());
        assertEquals(doctor, article.getAuthor());
    }

    @Test
    void testSetters() {
        Article article = new Article();
        Doctor doctor = new Doctor();
        LocalDateTime now = LocalDateTime.now();

        article.setId(2L);
        article.setTitle("Another Title");
        article.setContent("Another Content");
        article.setCategory("Wellness");
        article.setPublishedAt(now);
        article.setPublished(false);
        article.setAuthor(doctor);

        assertEquals(2L, article.getId());
        assertEquals("Another Title", article.getTitle());
        assertEquals("Another Content", article.getContent());
        assertEquals("Wellness", article.getCategory());
        assertEquals(now, article.getPublishedAt());
        assertFalse(article.isPublished());
        assertEquals(doctor, article.getAuthor());
    }

    @Test
    void testNoArgsConstructor() {
        Article article = new Article();
        assertNotNull(article);
    }

    @Test
    void testAllArgsConstructor() {
        Doctor doctor = new Doctor();
        LocalDateTime now = LocalDateTime.now();

        Article article = new Article(3L, "Title", "Content", "Category", now, true, doctor);

        assertEquals(3L, article.getId());
        assertEquals("Title", article.getTitle());
        assertEquals("Content", article.getContent());
        assertEquals("Category", article.getCategory());
        assertEquals(now, article.getPublishedAt());
        assertTrue(article.isPublished());
        assertEquals(doctor, article.getAuthor());
    }
}
