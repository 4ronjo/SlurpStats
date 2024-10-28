package com.example.slurpstats;

public class ConsumptionDetail {
    private int id;
    private int resultId;
    private int getraenkeId;
    private double menge;

    // Konstruktoren
    public ConsumptionDetail() {
    }

    public ConsumptionDetail(int id, int resultId, int getraenkeId, double menge) {
        this.id = id;
        this.resultId = resultId;
        this.getraenkeId = getraenkeId;
        this.menge = menge;
    }

    // Getter und Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getGetraenkeId() {
        return getraenkeId;
    }

    public void setGetraenkeId(int getraenkeId) {
        this.getraenkeId = getraenkeId;
    }

    public double getMenge() {
        return menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }
}
