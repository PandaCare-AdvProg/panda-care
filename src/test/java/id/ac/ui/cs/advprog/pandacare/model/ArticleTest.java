package id.ac.ui.cs.advprog.pandacare.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    @Test
    void testConstructorAndGetter() {
        Article A = new Article("T", "B", "L", "P");
        assertEquals("T", A.getTitle());
        assertEquals("B", A.getBody());
        assertEquals("L", A.getLink());
        assertEquals("P", A.getPicture());
    }

    @Test
    void testSetter() {
        Article A = new Article("T", "B", "L", "P");
        A.setTitle("T2");
        A.setBody("B2");
        A.setLink("L2");
        A.setPicture("P2");

        assertEquals("T2", A.getTitle());
        assertEquals("B2", A.getBody());
        assertEquals("L2", A.getLink());
        assertEquals("P2", A.getPicture());
    }
}