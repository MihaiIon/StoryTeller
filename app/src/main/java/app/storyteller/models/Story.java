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
    private boolean wasRead;
    private boolean isNew;

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
        this.isNew = true;
        this.wasRead = false;

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
    public boolean wasRead() { return wasRead;}
    public boolean isNew() { return isNew;}

    //------------------------------------------------------------
    // Setters

    public void setNew(boolean aNew) { isNew = aNew; }
    public void read() { this.wasRead = true; }

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
     * Returns all sentences as a long String
     */
    public String getContent() {
        String tmp = "";
        for (int i = 0; i < sentences.size(); i++) {
            tmp += sentences.get(i).getContent() + " ";
        }
        return tmp.substring(0,tmp.length()-1);
    }

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

