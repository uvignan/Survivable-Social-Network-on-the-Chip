package edu.cmu.sv.ws.ssnoc.dto;

/**
 * Created by vignan on 9/28/14.
 */

import com.google.gson.Gson;

import java.sql.Timestamp;

/**
 * This object contains user status information that is responded as part of the REST
 * API request.
 *
 */
public class Status {
    private long crumbID;
    private String userName;
    private String statusCode;
    private String createdDate;

    public long getCrumbidID() {
        return crumbID;
    }

    public void setCrumbID(long crumbID) {
        this.crumbID = crumbID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCreatedDate(){ return createdDate;}

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
