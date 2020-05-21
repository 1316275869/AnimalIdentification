package com.example.myapplication.androidclient;

import java.io.Serializable;

public class Collect implements Serializable{

    private String c_img;

    private String c_name ;
    private String c_briefdata;
    private String c_detailsurl;

    public  Collect() {
        // TODO Auto-generated constructor stub
    }

    public Collect(String c_img, String c_name, String c_briefdata, String c_detailsurl) {
        super();
        this.c_img = c_img;
        this.c_name = c_name;
        this.c_briefdata = c_briefdata;
        this.c_detailsurl = c_detailsurl;
    }

    public String getC_img() {
        return c_img;
    }

    public void setC_img(String c_img) {
        this.c_img = c_img;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_briefdata() {
        return c_briefdata;
    }

    public void setC_briefdata(String c_briefdata) {
        this.c_briefdata = c_briefdata;
    }

    public String getC_detailsurl() {
        return c_detailsurl;
    }


    public void setC_detailsurl(String c_detailsurl) {
        this.c_detailsurl = c_detailsurl;
    }

    public String tString() {
        return  c_img + "&" + c_name + "&" + c_briefdata + "&"
                + c_detailsurl;
    }

    public  static Collect splitMsg(String msg){
        String[] msgsplit=msg.split("&");
        if (msgsplit[1]==null){
            return null;
        }else {
            Collect collect=new Collect(msgsplit[0],msgsplit[1],msgsplit[2],msgsplit[3]);
            return collect;
        }
    }


}