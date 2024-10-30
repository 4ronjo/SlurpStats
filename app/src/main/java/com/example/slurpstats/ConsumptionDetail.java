package com.example.slurpstats;

public class ConsumptionDetail {
    private long id;
    private long resultId;
    private long drinkId;
    private double amount;

    public ConsumptionDetail() {}

    public ConsumptionDetail(long id, long resultId, long drinkId, double amount) {
        this.id = id;
        this.resultId = resultId;
        this.drinkId = drinkId;
        this.amount = amount;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getResultId() { return resultId; }
    public void setResultId(long resultId) { this.resultId = resultId; }

    public long getDrinkId() { return drinkId; }
    public void setDrinkId(long drinkId) { this.drinkId = drinkId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
