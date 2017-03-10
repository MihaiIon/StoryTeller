package app.storyteller.models;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Profile {
    private String name;
    private int tokens;
    private int google_id;
    private String imagePath;
    private Timestamp lastConnected;
    private ArrayList<Stories> favorites;

    public int getId() {
        return google_id;
    }

    public int getTokens() {
        return tokens;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLastvisited() { return lastConnected.toString(); }

    public ArrayList getFavorites() {
        return favorites;
    }

    public void setId(int google_id) {
        this.google_id = google_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLastConnected(Timestamp lastConnected) {
        this.lastConnected = lastConnected;
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

    public Profile(int google_id, String name, int tokens, String imagePath, Timestamp lastConnected, ArrayList<Stories> favorites) {
        this.name = name;
        this.tokens = tokens;
        this.google_id = google_id;
        this.imagePath = imagePath;
        this.lastConnected = lastConnected;
        this.favorites = favorites;

    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", tokens=" + tokens +
                ", id=" + google_id +
                ", imagepath='" + imagePath + '\'' +
                ", lastvisited='" + lastConnected.toString() + '\'' +
                ", favorites=" + favorites +
                '}';
    }
}
