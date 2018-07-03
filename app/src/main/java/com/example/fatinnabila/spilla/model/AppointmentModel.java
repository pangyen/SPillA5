package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 15/3/2018.
 */

public class AppointmentModel {

    private String atitle;
    private String atime;
    private String adate;
    private String aplace;

    public AppointmentModel() {}

    public AppointmentModel(String atitle, String adate, String atime, String aplace) {
        this.atitle = atitle;
        this.adate = adate;
        this.atime=atime;
        this.aplace=aplace;
    }
    public String getTitle() {
        return atitle;
    }
    public void setTitle(String title) {
        this.atitle = title;
    }
    public String getDate() {
        return adate;
    }
    public void setDate(String date) {
        this.adate = date;
    }
    public String getTime(){
        return atime;
    }
    public void setTime(String time){
        this.atime=time;
    }

    public String getPlace() {
        return aplace;
    }
    public void setPlace(String place) {
        this.aplace =place;
    }




}
