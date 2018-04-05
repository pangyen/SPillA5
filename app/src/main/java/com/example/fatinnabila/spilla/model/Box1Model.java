package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 1/4/2018.
 */

public class Box1Model {

    private String title;
    private String description;


    public Box1Model() {}

    public Box1Model(String title, String description) {
        this.title = title;
        this.description = description;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
