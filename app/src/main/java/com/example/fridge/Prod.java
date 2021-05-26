package com.example.fridge;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Prod {
    long id;
    String name;
    int days;
    String date1;
    static int ID = 1;
    long milliseconds ;
    String print1 = "";
    String print2 = "";

    public Prod(long id, String name, int days, long date){
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.id = id;
        this.name = name;
        this.days = days;
        this.milliseconds = date;
        this.print1 = formater.format(new Date(date));
        this.print2 = formater.format(new Date(date+days*86400000L));
        Log.d("seconds",Long.toString(this.milliseconds));
    }
    public Prod(long id, String name, int days) {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        this.id = id;
        this.name = name;

        this.date1 = date1;
        /*long longTime = (days*86400000L);
        expirationDate = formater.format(date2.getTime()+longTime);*/

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getDays() {
        return days;
    }

    public void setYear(int days) {
        this.days = days;
    }


    @Override
    public String toString() {
        return this.name + "\n" +"Добавлено: " +this.print1+ "\n" +"Истечёт "+this.print2;
    }
}
