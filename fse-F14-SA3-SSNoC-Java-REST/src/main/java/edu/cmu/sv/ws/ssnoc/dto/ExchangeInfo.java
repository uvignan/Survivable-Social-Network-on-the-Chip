package edu.cmu.sv.ws.ssnoc.dto;

/**
 * Created by vignan on 10/8/14.
 */
public class ExchangeInfo {
    private String author;
    private String target;
    private String postedAt;
    private String location;
    private String content;
    private String imgPath;
    private float latitude;
    private float longitude;

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

    public void setImgPath(String imgPath){this.imgPath=imgPath;}

    public float getLatitude(){return latitude;}

    public void setLatitude(float latitude ){this.latitude=latitude;}

    public float getLongitude(){return longitude;}

    public void setLongitude(float longitude){this.longitude=longitude;}

}
