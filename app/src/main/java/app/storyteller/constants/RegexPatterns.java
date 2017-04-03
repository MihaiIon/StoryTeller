package app.storyteller.constants;

/**
 * Created by Mihai on 2017-04-02.
 */

public class RegexPatterns {
    public static final String CHARACTER_VALIDATION =
            "\\s|\\w|\\d|[áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ!?.,'%$#*]";
    public static final String BASIC_INPUT_VALIDATION =
            "^.(?:\\s?.)*$";
    public static final String STORY_SENTENCE_VALIDATION =
            "";
}
