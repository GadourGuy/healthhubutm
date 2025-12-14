package com.secj3303.model;

import java.util.Calendar;
import java.util.Date;

public class BmiResultDto {
    private String name;
    private int age;
    private double height;
    private String heightUnit = "cm";
    private double weight;
    private String weightUnit = "kg";
    private double bmiValue;
    private String category;
    private Date calculatedDate;
    private String[] interests;

    public BmiResultDto(Person p, double bmi, String cat) {
        this.name = p.getName();
        // Calculate age from YOB
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        this.age = currentYear - p.getYob();
        this.height = p.getHeight();
        this.weight = p.getWeight();
        this.bmiValue = bmi;
        this.category = cat;
        this.calculatedDate = new Date();
        this.interests = p.getInterest();
    }

    // Getters are required for Thymeleaf to read the properties
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getHeight() { return height; }
    public String getHeightUnit() { return heightUnit; }
    public double getWeight() { return weight; }
    public String getWeightUnit() { return weightUnit; }
    public double getBmiValue() { return bmiValue; }
    public String getCategory() { return category; }
    public Date getCalculatedDate() { return calculatedDate; }
    public String[] getInterests() { return interests; }
}