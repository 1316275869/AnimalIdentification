package com.example.myapplication.Detaile;

public class DetailData {

    private String title;
    private String detail;
    private String ur;

    public DetailData(String title, String detail, String ur) {
        this.title = title;
        this.detail = detail;
        this.ur = ur;
    }

    public DetailData(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }
}
