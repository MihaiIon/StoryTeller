package app.storyteller.models;

/**
 * Created by Vincent on 2017-03-09.
 */

public class Sentences {
    private String author;
    private String content;

    public Sentences(String content,String author) {
        setContent(content);
        setAuthor(author);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
