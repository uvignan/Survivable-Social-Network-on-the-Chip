package edu.cmu.sv.ws.ssnoc.data.po;

/**
 * Created by vignan on 10/8/14.
 */
public class ExchangeInfoPO {
    public long messageID;
    public String author;
    public String target;
    public String postedAt;
    public String location;
    public String content;
    public String imgPath;
    public float latitude;
    public float longitude;

    public long getMessageID(){return messageID;}

    public void setMessageID(long messageID){this.messageID=messageID;}

    public String getAuthor(){return author;}

    public void setAuthor(String author){this.author=author;}

    public String getTarget(){return target;}

    public void setTarget(String target){this.target=target;}

    public String getPostedAt(){return postedAt;}

    public void  setPostedAt(String postedAt){this.postedAt=postedAt;}

    public String getLocation(){return location;}

    public void setLocation(String location){this.location=location;}

    public String getContent(){return content;}

    public void setContent(String content){this.content=content;}

    public String getImgPath(){return imgPath;}

    public String setImgPath(String imgPath){return this.imgPath=imgPath;}

    public float getLatitude(){return latitude;}

    public void setLatitude(float latitude ){this.latitude=latitude;}

    public float getLongitude(){return longitude;}

    public void setLongitude(float longitude){this.longitude=longitude;}

}
