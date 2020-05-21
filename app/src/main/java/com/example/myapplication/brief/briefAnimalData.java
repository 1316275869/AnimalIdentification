package com.example.myapplication.brief;

import android.graphics.Bitmap;

public class briefAnimalData {
    private String img;
    private String name;
    private String briefData;
    private String detailsUrl;

    public briefAnimalData(String i,String n,String b,String url){
        this.img=i;
        this.name=n;
        this.briefData=b;
        this.detailsUrl=url;
    }
    public String getImg() {
        return img;
    }

    public void setImgid(String imgid) {
        this.img = imgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBriefData() {
        return briefData;
    }

    public void setBriefData(String briefData) {
        this.briefData = briefData;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
    public  static briefAnimalData splitMsg(String msg){
        String[] msgsplit=msg.split("&");
        if (msgsplit[1]==null){
            return null;
        }else {
            briefAnimalData collect=new briefAnimalData(msgsplit[0],msgsplit[1],msgsplit[2],msgsplit[3]);
            return collect;
        }
    }
}
