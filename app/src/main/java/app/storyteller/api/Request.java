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

    /**
     * Constructor.
     *
     * @param action :  The action to be executed by the API.
     * @param json   :
     */
    Request(String action, JSONObject json){
        this.action = action;
        this.params = null;
        this.json   = json;
    }

    /**
     * Constructor.
     *
     * @param action :  The action to be executed by the API.
     * @param params :  The parameters related to the action that will be executed by the API.
     * @param json   :
     */
    Request(String action, String[] params, JSONObject json){
        this.action = action;
        this.params = params;
        this.json   = json;
    }


    //------------------------------------------------------------------------
    // Getters

    String getAction()      { return action; }
    JSONObject getJSON()    { return json;   }

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
