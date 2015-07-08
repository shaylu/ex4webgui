/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.json;

import com.google.gson.Gson;

/**
 *
 * @author Dell
 */
public class JsonMessage {
    public static Gson gson;
    static {
        gson = new Gson();
    }
    
    public static enum Status {
        Error, Success;
    }
    public Status status;
    public String message;

    public JsonMessage(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
