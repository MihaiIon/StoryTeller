package app.storyteller.api;

import org.json.JSONObject;

/**
 * Created by Mihai on 2017-01-30.
 */
class Request {

    /*
     * Attributes.
     */
    private String action;
    private String[] params;
    private JSONObject json;
    private boolean needsResponse;

    /**
     * Constructor.
     *
     * @param action :  The action to be executed by the API.
     * @param json   :
     * @param needsResponse : Indicates if the API will respond with that or not.
     */
    Request(String action, JSONObject json, boolean needsResponse){
        this.action = action;
        this.params = null;
        this.json   = json;
        this.needsResponse = needsResponse;
    }

    /**
     * Constructor.
     *
     * @param action :  The action to be executed by the API.
     * @param params :  The parameters related to the action that will be executed by the API.
     * @param json   :
     * @param needsResponse : Indicates if the API will respond with that or not.
     */
    Request(String action, String[] params, JSONObject json, boolean needsResponse){
        this.action = action;
        this.params = params;
        this.json   = json;
        this.needsResponse = needsResponse;
    }


    //------------------------------------------------------------------------
    // Getters

    String getAction()      { return action;        }
    JSONObject getJSON()    { return json;          }
    boolean needsResponse() { return needsResponse; }

    /**
     * Returns a well formatted URL to access the API.
     */
    String getUrl(){
        return Api.API_URL + action + buildParams();
    }



    //------------------------------------------------------------------------
    // Methods

    /**
     * Returns a string containing all parameters (URL formatted).
     *
     * Ex: "param[0]/param[1]/param[...]"
     */
    private String buildParams(){
        if (params != null ) {
            String output = "/";
            for (String param : params)
                output += param + "/";
            return output.substring(0, output.length() - 1);
        }
        else return "";
    }
}
