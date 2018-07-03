package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 1/4/2018.
 */

public class Box1Model {

    private String title;
    private String box1Id;

    public Box1Model() {}

    public Box1Model(String box1Id, String title) {

        this.box1Id=box1Id;
        this.title=title;
    }
    public String getBox1Id() {
        return box1Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
