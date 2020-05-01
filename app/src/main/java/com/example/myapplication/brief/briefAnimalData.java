package com.example.myapplication.brief;

import android.graphics.Bitmap;

public class briefAnimalData {
    private Bitmap img;
    private String name;
    private String briefData;
    private String detailsUrl;

    public briefAnimalData(Bitmap i,String n,String b,String url){
        this.img=i;
        this.name=n;
        this.briefData=b;
        this.detailsUrl=url;
    }
    public Bitmap getImg() {
        return img;
    }

    public void setImgid(Bitmap imgid) {
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
}
