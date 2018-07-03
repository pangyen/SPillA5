package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 16/3/2018.
 */

public class HistoryModel {


    private String htitle;
    private String hdesciption;
    private String hstart;
    private String hend;

    public HistoryModel() {}


    public HistoryModel(String htitle, String hdesciption, String hstart, String hend) {
        this.htitle = htitle;
        this.hdesciption = hdesciption;
        this.hstart=hstart;
        this.hend=hend;

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

    public String getStart() {
        return hstart;
    }

    public void setStart(String start) {
        this.hstart = start;
    }

    public String getEnd() {
        return hend;
    }

    public void setEnd(String end) {
        this.hend = end;
    }
}
