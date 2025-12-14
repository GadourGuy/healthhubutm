package com.secj3303.model;

public class Person {
    private String name;
    private int yob;
    private double weight;
    private double height;
    private double bmi;
    private String category;
    private int id;
    private String[] interest;

    public Person(){

    }
    public Person(String name, int yob, double weight, double height, String[] interest) {
        this.name = name;
        this.yob = yob;
        this.weight = weight;
        this.height = height;
        this.interest = interest;
        calculateBmi();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYob() {
        return yob;
    }

    public void setYob(int yob) {
        this.yob = yob;
    }

    public double getWeight() {
        return weight;
    }

    public int getAge() {
        return 2025 - yob;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setBMI(double bmi) {
        this.bmi = bmi;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setInterest(String[] interest) {
        this.interest = interest;
    }

    public String[] getInterest() {
        return interest;
    }

    public double getBmi() {
        return bmi;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    //added to correct logic
    private void calculateBmi() {
            this.bmi = weight / Math.pow(height, 2);
            categorize();
    }

    public void categorize() {
        if (bmi < 18.5) {
            category = "Underweight";
        } 
        else if (bmi < 25) {
            category = "Normal";
        } 
        else if (bmi < 30) {
            category = "Overweight";
        } 
        else {
        category = "Obesity";
        }
    }
}
