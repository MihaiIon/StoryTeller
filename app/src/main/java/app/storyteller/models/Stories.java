package app.storyteller.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Vincent on 2017-03-09.
 */

public class Stories {
    private String name;
    private String creator;
    private String[] collaborators;
    private String theme; //PENSER A FAIRE UN ENUM POUR FILTRER
    private ArrayList<Sentences> sentences;

    public Stories(String name,String creator,String theme,String sentences) {
        setName(name);
        setCreator(creator);
        setTheme(theme);
        addSentences(new Sentences(sentences,creator));
    }

    //return the name of the story
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    //return the creator of the story
    public String getCreator() {
        return creator;
    }


    public void setCreator(String creator) {
        this.creator = creator;
    }

    //return the collaborators
    public String[] getCollaborators() {
        return collaborators;
    }

    public void setCollaborator(String[] collaborator) {
        this.collaborators = collaborator;
    }

    //return an the whole sotry in an ArrayList
    public ArrayList<Sentences> getSentences() {
        return sentences;
    }


    public void addSentences(Sentences sentences) {
        this.sentences.add(sentences);
    }

    //TO DO
    public String getTheme() {
        return theme;
    }
    //TO DO
    public void setTheme(String theme) {
        this.theme = theme;
    }


    public String getLastAuthor(){
        return sentences.get(sentences.size()-1).getAuthor();
    }

    //return everyone who wrote in the story
    public ArrayList<String> getAuthors(){
        ArrayList<String> liste = new ArrayList<String>();
        liste.add(this.getCreator());
        for(int i=0;i<this.getCollaborators().length;i++){
            liste.add(getCollaborators()[i]);
        }
        return liste;
    }

    //return the number of sentences in story
    public int getStorieslength(){
        return sentences.size();
    }


}

