package com.example.slurpstats;

public class Drink {
    private int id;
    private String name;
    private double alkoholgehalt;

    // Konstruktoren
    public Drink() {
    }

    public Drink(int id, String name, double alkoholgehalt) {
        this.id = id;
        this.name = name;
        this.alkoholgehalt = alkoholgehalt;
    }

    // Getter und Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAlkoholgehalt() {
        return alkoholgehalt;
    }

    public void setAlkoholgehalt(double alkoholgehalt) {
        this.alkoholgehalt = alkoholgehalt;
    }
}
