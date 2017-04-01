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

    /**
     * Default Constructor.
     */
    public Sentence(int id, User author, String content, Timestamp creationDate) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.creationDate = creationDate;
    }

    /**
     * Quick Constructor
     * -- Used for editing a Story (See AsyncTask).
     */
    public Sentence(String content) {
        this.id = -1;
        this.content = content;
        this.author = null;
        this.creationDate = null;
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
