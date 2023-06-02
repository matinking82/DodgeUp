package org.example.Models;

public class GameRecord {
    private int id;
    private String date;
    private int points;

    public GameRecord(){

    }
    public GameRecord(String date,int points){
        this.date = date;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
