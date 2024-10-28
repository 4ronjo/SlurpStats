package com.example.slurpstats;

public class Drink {
    private long id;
    private String name;
    private double alcoholContent;

    public Drink() {}

    public Drink(long id, String name, double alcoholContent) {
        this.id = id;
        this.name = name;
        this.alcoholContent = alcoholContent;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getAlcoholContent() { return alcoholContent; }
    public void setAlcoholContent(double alcoholContent) { this.alcoholContent = alcoholContent; }
}
