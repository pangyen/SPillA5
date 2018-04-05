package com.example.fatinnabila.spilla.model;

/**
 * Created by fatin nabila on 15/3/2018.
 */

public class AppointmentModel {

    private String atitle;
    private String atime;
    private String adate;

    public AppointmentModel() {}

    public AppointmentModel(String atitle, String adate, String atime) {
        this.atitle = atitle;
        this.adate = adate;
        this.atime=atime;

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




}
