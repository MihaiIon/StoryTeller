package app.storyteller.models;

import java.sql.Timestamp;

/**
 * Created by Vincent on 2017-03-09.
 */

public class Sentence {

    private int id;
    private String content;
    private User author;
    private Timestamp creationDate;

    public Sentence(int id, User author, String content, Timestamp creationDate) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.creationDate = creationDate;
    }

    public int getId() { return  id; }
    public String getContent() {
        return content;
    }
    public User getAuthor() {
        return author;
    }
    public Timestamp getCreationDate() { return creationDate; }

    /**
     *
     */
    @Override
    public String toString() {
        return "Sentence{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", creationDate=" + creationDate +
                '}';
    }
}
