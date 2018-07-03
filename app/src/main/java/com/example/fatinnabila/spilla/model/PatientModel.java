package com.example.fatinnabila.spilla.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by fatin nabila on 21/3/2018.
 */
@IgnoreExtraProperties
public class PatientModel {

    private List<String> patients;

    public PatientModel() {
    }

    public PatientModel(List<String> patients) {
        this.patients = patients;
    }

    public List<String> getPatients() {
        return patients;
    }

    public void setPatients(List<String> patients) {
        this.patients = patients;
    }
}
