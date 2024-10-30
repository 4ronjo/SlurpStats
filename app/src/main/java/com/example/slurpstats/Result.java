package com.example.slurpstats;

public class Result {
    private long id;
    private String gender;
    private double weight;
    private double bloodAlcoholContent;
    private String date;
    private String title;

    public Result() {}

    public Result(long id, String gender, double weight, double bloodAlcoholContent, String date, String title) {
        this.id = id;
        this.gender = gender;
        this.weight = weight;
        this.bloodAlcoholContent = bloodAlcoholContent;
        this.date = date;
        this.title = title;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getBloodAlcoholContent() { return bloodAlcoholContent; }
    public void setBloodAlcoholContent(double bloodAlcoholContent) { this.bloodAlcoholContent = bloodAlcoholContent; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
