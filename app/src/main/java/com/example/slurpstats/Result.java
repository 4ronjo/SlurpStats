package com.example.slurpstats;

public class Result {
    private int id;
    private String geschlecht;
    private double gewicht;
    private double blutalkoholwert;

    // Konstruktoren
    public Result() {
    }

    public Result(int id, String geschlecht, double gewicht, double blutalkoholwert) {
        this.id = id;
        this.geschlecht = geschlecht;
        this.gewicht = gewicht;
        this.blutalkoholwert = blutalkoholwert;
    }

    // Getter und Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    public double getBlutalkoholwert() {
        return blutalkoholwert;
    }

    public void setBlutalkoholwert(double blutalkoholwert) {
        this.blutalkoholwert = blutalkoholwert;
    }
}
