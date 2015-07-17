package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class Search {
    private String[] content;
    private String type;


    public String[] getContent(){return content;}

    public void setContent(String[] content){this.content=content;}

    public String getType(){return type;}

    public void setType(String type){this.type=type;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
