package app.storyteller.models;

import java.util.ArrayList;

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
    public Story(int id, StoryDetails details, User creator, ArrayList<Sentence> sentences) {
        this.id = id;
        this.details = details;
        this.creator = creator;
        this.sentences = sentences;

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
}

