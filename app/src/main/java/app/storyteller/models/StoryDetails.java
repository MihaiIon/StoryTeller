package app.storyteller.models;

/**
 * Created by Mihai on 2017-03-10.
 */
public class StoryDetails {

    private String title;
    private String theme;
    private String mainCharacter;

    public StoryDetails(String title, String theme, String character){
        this.title = title;
        this.theme = theme;
        this.mainCharacter = character;
    }

    public String getTitle() { return title; }
    public String getTheme() {
        return theme;
    }
    public String getMainCharacter() { return mainCharacter; }
}
