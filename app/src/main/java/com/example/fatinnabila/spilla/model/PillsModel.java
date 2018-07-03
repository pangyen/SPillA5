package com.example.fatinnabila.spilla.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by root on 28/10/2017.
 */
@IgnoreExtraProperties
public class PillsModel {

    private String title;
    private String description;
    private String dose;
    private String purpose;
    private String effect;

    public PillsModel() {}

    public PillsModel(String title, String description, String dose, String effect, String purpose) {
        this.title = title;
        this.description = description;
        this.dose = dose;
        this.effect=effect;
        this.purpose=purpose;
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

    public String getDose() {
        return dose;
    }

    public void dose(String dose) {
        this.dose = dose;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
