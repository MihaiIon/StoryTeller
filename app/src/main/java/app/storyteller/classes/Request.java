package app.storyteller.classes;

/**
 * Created by Mihai on 2017-01-30.
 */
public class Request {

    /**
     * TODO.
     */
    public final static class Actions{
        public final static String CREATE_PROFILE = "createprofile";
        public final static String RESET_DATABASE = "resetdatabase";
    }

    /*
     * Attributes.
     */
    private String action;
    private String[] params;
    private boolean needsResponse;

    /**
     * Constructor.
     *
     * @param action :  The action to be executed by the API.
     * @param params :  The parameters related to the action that will be executed by the API.
     * @param needsResponse : Indicates if the API will respond with that or not.
     */
    public Request(String action, String[] params, boolean needsResponse){
        this.action = action;
        this.params = params;
        this.needsResponse = needsResponse;
    }

    /*
     * Getters.
     */
    public String getAction()       { return action + "/"; }
    public boolean needsResponse() { return needsResponse; }

    /**
     * Returns a string containing all parameters (URL formatted).
     *
     * Ex: "param[0]/param[1]/param[...]"
     */
    public String getParams(){
        String output = "";
        for (String param: params)
            output += param + "/";
        return output.substring(0, output.length()-1);
    }
}
