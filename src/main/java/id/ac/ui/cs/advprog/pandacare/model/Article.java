package id.ac.ui.cs.advprog.pandacare.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Article {
    private String Title;
    private String Body;
    private String Link;
    private String Picture;

    public Article(String Title, String Body, String Link, String Picture) {
        this.Title = Title;
        this.Body = Body;
        this.Link = Link;
        this.Picture = Picture;
    }
}
