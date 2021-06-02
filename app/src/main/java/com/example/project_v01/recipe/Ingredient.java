package com.example.project_v01.recipe;

public class Ingredient {
    private String name;
    private double amount;
    private String measurement;

    public Ingredient(String name, double amount, String measurement) {
        this.name = name;
        this.amount = amount;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String toString() {
        return name + ": " + amount + " " + measurement;


    }

}
