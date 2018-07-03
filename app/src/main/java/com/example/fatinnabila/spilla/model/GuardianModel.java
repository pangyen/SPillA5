package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 21/3/2018.
 */

public class GuardianModel {
    private String title;
    private String description;
    private String email;


    public GuardianModel() {}

    public GuardianModel(String title, String description, String email) {
        this.title = title;
        this.description = description;
        this.email= email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
