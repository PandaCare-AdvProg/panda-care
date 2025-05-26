package id.ac.ui.cs.advprog.pandacare.repository;

import id.ac.ui.cs.advprog.pandacare.model.Article;
import id.ac.ui.cs.advprog.pandacare.model.Doctor;
import id.ac.ui.cs.advprog.pandacare.enums.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    private Doctor createTestDoctor(String email) {
        Doctor doctor = new Doctor(
                email,
                "password",
                "Dr. John",
                "1234567890",
                "Some Address",
                "Hospital Address",
                "0987654321",
                Role.DOCTOR,
                "General"
        );
        entityManager.persist(doctor);
        entityManager.flush();
        return doctor;
    }

    @Test
    public void testFindByPublishedTrueOrderByPublishedAtDesc() {
        Doctor doctor = createTestDoctor("doctor1@example.com");

        Article article1 = Article.builder()
                .title("Article 1")
                .content("Content 1")
                .category("General")
                .published(true)
                .publishedAt(LocalDateTime.now().minusDays(1))
                .author(doctor)
                .build();

        Article article2 = Article.builder()
                .title("Article 2")
                .content("Content 2")
                .category("Health")
                .published(true)
                .publishedAt(LocalDateTime.now())
                .author(doctor)
                .build();

        Article article3 = Article.builder()
                .title("Article 3")
                .content("Content 3")
                .category("News")
                .published(false)
                .publishedAt(LocalDateTime.now())
                .author(doctor)
                .build();

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        articleRepository.flush();

        List<Article> publishedArticles =
                articleRepository.findByPublishedTrueOrderByPublishedAtDesc();

        assertEquals(2, publishedArticles.size());
        assertEquals("Article 2", publishedArticles.get(0).getTitle());
        assertEquals("Article 1", publishedArticles.get(1).getTitle());
    }

    @Test
    public void testFindByPublishedTrueAndCategoryIgnoreCaseOrderByPublishedAtDesc() {
        Doctor doctor = createTestDoctor("doctor2@example.com");

        Article article1 = Article.builder()
                .title("Health Article 1")
                .content("Content 1")
                .category("HEALTH")
                .published(true)
                .publishedAt(LocalDateTime.now().minusHours(2))
                .author(doctor)
                .build();

        Article article2 = Article.builder()
                .title("Health Article 2")
                .content("Content 2")
                .category("health")
                .published(true)
                .publishedAt(LocalDateTime.now().minusHours(1))
                .author(doctor)
                .build();

        Article article3 = Article.builder()
                .title("General Article")
                .content("Content 3")
                .category("General")
                .published(true)
                .publishedAt(LocalDateTime.now())
                .author(doctor)
                .build();

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        List<Article> healthArticles =
                articleRepository.findByPublishedTrueAndCategoryIgnoreCaseOrderByPublishedAtDesc("HeAlTh");

        assertEquals(2, healthArticles.size());
        assertTrue(healthArticles.get(0).getPublishedAt().isAfter(healthArticles.get(1).getPublishedAt()));
    }
}
