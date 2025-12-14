package com.secj3303.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "person") // This tells Hibernate to create a 'person' table
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "yob")
    private int yob;

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private double height;

    @Column(name = "bmi")
    private double bmi;

    @Column(name = "category")
    private String category;

    @Transient
    private String[] interest;

    @Column(name = "interests")
    private String interestsDb;

    
    public Person() {
    }

    public Person(String name, int yob, double weight, double height, String[] interest) {
        this.name = name;
        this.yob = yob;
        this.weight = weight;
        this.height = height;
        this.interest = interest;
        
        // Convert array to String for Database storage
        updateInterestsDb();
        
        calculateBmi();
    }

    // --- ID ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // --- Name ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // --- YOB ---
    public int getYob() {
        return yob;
    }

    public void setYob(int yob) {
        this.yob = yob;
    }

    public int getAge() {
        return 2025 - yob;
    }

    // --- Weight ---
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        calculateBmi(); // Recalculate if weight changes
    }

    // --- Height ---
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        calculateBmi(); // Recalculate if height changes
    }

    // --- BMI & Category ---
    public double getBmi() {
        return bmi;
    }

    public void setBMI(double bmi) {
        this.bmi = bmi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void calculateBmi() {
        // Fix: Convert Height from CM to Meters if needed
        // Assuming input might be 170 (cm) -> 1.7 (m)
        double h = this.height;
        if (h > 3.0) { 
            h = h / 100.0; // Convert cm to meters
        }
        
        if (h > 0) {
            this.bmi = weight / (h * h);
            // Round to 2 decimal places
            this.bmi = Math.round(this.bmi * 100.0) / 100.0;
            categorize();
        }
    }

    public void categorize() {
        if (bmi < 18.5) {
            category = "Underweight";
        } else if (bmi < 25) {
            category = "Normal";
        } else if (bmi < 30) {
            category = "Overweight";
        } else {
            category = "Obesity";
        }
    }


    
    public String[] getInterest() {
        if (interest == null && interestsDb != null && !interestsDb.isEmpty()) {
            interest = interestsDb.split(",");
        }
        return interest;
    }

    public void setInterest(String[] interest) {
        this.interest = interest;
        updateInterestsDb();
    }
    
    private void updateInterestsDb() {
        if (this.interest != null && this.interest.length > 0) {
            this.interestsDb = String.join(",", this.interest);
        } else {
            this.interestsDb = "";
        }
    }
    
    public String getInterestsDb() {
        return interestsDb;
    }
    
    public void setInterestsDb(String interestsDb) {
        this.interestsDb = interestsDb;
        if (interestsDb != null && !interestsDb.isEmpty()) {
            this.interest = interestsDb.split(",");
        }
    }
}