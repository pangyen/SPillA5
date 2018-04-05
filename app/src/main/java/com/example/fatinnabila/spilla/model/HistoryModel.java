package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 16/3/2018.
 */

public class HistoryModel {


    private String htitle;
    private String hdesciption;

    public HistoryModel() {}


    public HistoryModel(String htitle, String hdesciption) {
        this.htitle = htitle;
        this.hdesciption = hdesciption;

    }

    public String getTitle() {
        return htitle;
    }

    public void setTitle(String title) {
        this.htitle = title;
    }

    public String getDescription() {
        return hdesciption;
    }

    public void setDescription(String description) {
        this.hdesciption = description;
    }
}
