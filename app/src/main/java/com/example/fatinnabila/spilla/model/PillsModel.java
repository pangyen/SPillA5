package com.example.fatinnabila.spilla.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by root on 28/10/2017.
 */
@IgnoreExtraProperties
public class PillsModel {

    private String title;
    private String description;
    private long createdAt;

    public PillsModel() {}

    public PillsModel(String title, String description, long createdAt) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
