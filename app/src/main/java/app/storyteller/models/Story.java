package app.storyteller.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Vincent on 2017-03-09.
 */
public class Story {

    /*
     * Attributes.
     */
    private int id;             // Index in the database.
    private User creator;
    private Collaborator[] collaborators;
    private ArrayList<Sentence> sentences;
    private StoryDetails details;
    private Timestamp creationDate;

    /**
     * Available Themes.
     */
    public static class Themes{
        public static final String FUNNY = "funny";
        public static final String HORROR = "horror";
        // ...
    }

    /**
     * Constructor.
     */
    public Story(int id, StoryDetails details, User creator, ArrayList<Sentence> sentences, Timestamp creationDate) {
        this.id = id;
        this.details = details;
        this.creator = creator;
        this.sentences = sentences;
        this.creationDate = creationDate;

        // TODO. -> Get collaborators from sentences.
    }


    //------------------------------------------------------------
    // Getters

    public int getId() { return id; }
    public StoryDetails getDetails() { return details; }
    public User getCreator() {
        return creator;
    }
    public Collaborator[] getCollaborators() {
        return collaborators;
    }
    public ArrayList<Sentence> getSentences() {
        return sentences;
    }
    public Timestamp getCreationDate() { return creationDate; }


    //------------------------------------------------------------
    // Methods

    /**
     * -- Why?
     */
    public User getLastAuthor(){
        return sentences.get(sentences.size()-1).getAuthor();
    }

    /**
     * Returns the Profile of every person that participated to
     * the current story.
     */
    public ArrayList<User> getParticipants(){
        ArrayList<User> list = new ArrayList<User>();
        list.add(this.getCreator());
        for(int i=0;i<this.getCollaborators().length;i++){
            list.add(getCollaborators()[i].getUser());
        }
        return list;
    }

    /**
     * Returns the number of sentences in story
     */
    public int getStorySize(){
        return sentences.size();
    }

    /**
     *
     */
    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", creator=" + creator +
                ", collaborators=" + Arrays.toString(collaborators) +
                ", sentences=" + sentences +
                ", details=" + details +
                '}';
    }
}

