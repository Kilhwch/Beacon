package com.example.kasper.beacon.SupportClasses;

/**
 * Created by kasper on 4/20/2015.
 */
public class AvrgDistance {

    private final int MAX = 4;
    private final int OFFSET = 4;
    private int counter = 0;
    private double[] average;
    public double currentAverage = 0;


    public AvrgDistance() {
        average = new double[MAX];
    }

    public void calculate(double newDistance, double currentAverage) {
        if (counter < MAX) {
            if ((currentAverage + OFFSET) > newDistance) {
                average[counter] = newDistance;
                counter++;
            }
        }
        else {
            double newAverage = countAverage();
            this.currentAverage = newAverage;
            average = new double[MAX];
            counter = 0;
        }
    }

    private Double countAverage() {
        double avgDistance = 0;
        for (int i = 0; i < average.length; i++) {
            avgDistance += average[i];
        }
        return (avgDistance / MAX);
    }

    public double getCurrentAverage() {
        return currentAverage;
    }
}