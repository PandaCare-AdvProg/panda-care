package id.ac.ui.cs.advprog.pandacare.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class Article {
    private String title;
    private String body;
    private String link;
    private String picture;
}
