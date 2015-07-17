package edu.cmu.sv.ws.ssnoc.data.po;

/**
 * Created by vignan on 9/28/14.
 */

import com.google.gson.Gson;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * This is the persistence class to save all user status information in the system.
 * This contains information like the user's name, his status, his status createdAt
 * information entered by the user when updating his status
 * Information is saved in SSN_STATUS_CRUMB table.
 *
 */
public class StatusPO {
    private String userName;
    private String statusCode;
    private String createdDate;
    private long crumbID;


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

    public String getCreatedDate() { return createdDate; }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public long getCrumbID() {
        return crumbID;
    }

    public void setCrumbID(long crumbID) {
        this.crumbID = crumbID;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
