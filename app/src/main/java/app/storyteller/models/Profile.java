package app.storyteller.models;

import java.util.ArrayList;

public class Profile {
    private String name;
    private int tokens;
    private int id; //google id
    private String imagepath;
    private String lastvisited;
    private ArrayList<Stories> favorites;

    public int getId() {
        return id;
    }

    public int getTokens() {
        return tokens;
    }

    public String getName() {
        return name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public String getLastvisited() {
        return lastvisited;
    }

    public ArrayList getFavorites() {
        return favorites;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public void setLastvisited(String lastvisited) {
        this.lastvisited = lastvisited;
    }

    public void addFavorite(Stories story) {
        int i = this.favorites.indexOf(story); //return 0 si la story n'est pas dans la liste
        if(i < 0) {
            this.favorites.add(story);
        }
        else throw new IllegalArgumentException("Story already present");

    }

    public void removeFavorite(Stories story) {
        int i = this.favorites.indexOf(story);
        if(i >= 0) {
            this.favorites.remove(i);
        }
        else throw new IllegalArgumentException("Story is not present");
    }

    public Profile(int id, String name, int tokens, String imagepath, String lastvisited, ArrayList<Stories> favorites) {
        this.name = name;
        this.tokens = tokens;
        this.id = id;
        this.imagepath = imagepath;
        this.lastvisited = lastvisited;
        this.favorites = favorites;

    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", tokens=" + tokens +
                ", id=" + id +
                ", imagepath='" + imagepath + '\'' +
                ", lastvisited='" + lastvisited + '\'' +
                ", favorites=" + favorites +
                '}';
    }
}
