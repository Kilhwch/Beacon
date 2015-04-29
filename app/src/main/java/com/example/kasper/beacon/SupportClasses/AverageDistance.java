package com.example.kasper.beacon.SupportClasses;

/**
 * Created by kasper on 4/20/2015.
 *
 * This class calculates average distance of the Beacon.
 * 1. Take 4 distance values and count their average into avg1 (*these 4 values are often similar)
 * 2. Take 4 distance values and count their average into avg2 (*these 4 values are often similar)
 * 3. Take the average of avg1 and avg2
 */
public class AverageDistance {

    private final int MAX = 4;
    private double[] average;
    private int counter = 0;
    private double avg1, avg2 = 0;
    public double totalAverage;

    public AverageDistance() {
        average = new double[MAX];
    }

    public void add(double newDistance) {
        if (counter < MAX) {
            average[counter] = newDistance;
            counter++;
        } else {
            if (avg1 == 0) {
                avg1 = countAverage();
            } else if (avg2 == 0) {
                avg2 = countAverage();
                totalAverage = ((avg1 + avg2) / 2);
                avg1 = 0;
                avg2 = 0;
            }
        }
    }

    private Double countAverage() {
        double avgDistance = 0;
        for (int i = 0; i < average.length; i++) {
            avgDistance += average[i];
        }
        counter = 0;
        return (avgDistance / MAX);
    }

    public double getCurrentAverage() {
        return totalAverage;
    }
}