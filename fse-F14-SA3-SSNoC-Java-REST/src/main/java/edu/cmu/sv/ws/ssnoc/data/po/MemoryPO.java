package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

import java.sql.Timestamp;

/**
 * Created by Vignan on 10/14/2014.
 */
public class MemoryPO {

    private String crumbID;
    private long usedVolatile;
    private long remainingVolatile;
    private long usedPersistent;
    private long remainingPersistent;
    private String createdAt;

    public void setCrumbID(String crumbID){this.crumbID=crumbID;}

    public String getCrumbID(){return crumbID;}

    public void setUsedVolatile(long usedVolatile){this.usedVolatile=usedVolatile;}

    public long getUsedVolatile(){return usedVolatile;}

    public void setRemainingVolatile(long remainingVolatile){this.remainingVolatile=remainingVolatile;}

    public long getRemainingVolatile(){return remainingVolatile;}

    public void setUsedPersistent(long usedPersistent){this.usedPersistent=usedPersistent;}

    public long getUsedPersistent(){return usedPersistent;}

    public void setRemainingPersistent(long remainingPersistent){this.remainingPersistent=remainingPersistent;}

    public long getRemainingPersistent(){return remainingPersistent;}

    public void setCreatedAt(String createdAt){this.createdAt=createdAt;}

    public String getCreatedAt(){return createdAt;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
