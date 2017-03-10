package app.storyteller.models;

import java.sql.Timestamp;

/**
 * Created by Vincent on 2017-03-09.
 */
public class Sentence {

    private String content;
    private User author;
    private Timestamp timestamp;

    public Sentence(String content, User author, Timestamp timestamp) {
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }
    public User getAuthor() {
        return author;
    }
    public Timestamp getTimestamp() { return timestamp; }
}
