package app.storyteller.models;

public class Profile {
    private String name;
    private int tokens;
    private int id; //google id
    private String imagepath;
    private String lastvisited;

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

    public Profile(String name, int tokens, int id, String imagepath, String lastvisited) {
        this.name = name;
        this.tokens = tokens;
        this.id = id;
        this.imagepath = imagepath;
        this.lastvisited = lastvisited;
    }


}
