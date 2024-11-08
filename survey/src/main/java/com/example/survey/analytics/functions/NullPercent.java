package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

public class NullPercent {

    // Method for Integer array
    public AnalysisReturnObject func(Integer[] array) {
        if (array == null || array.length == 0) {
            return new AnalysisReturnObject((float) 0.0, "rFloat"); // Return 0% if the array is empty or null
        }

        int nullCount = 0;
        for (Integer num : array) {
            if (num == null) {
                nullCount++; // Increment the count for each null value
            }
        }

        Float nullPercentage = (float) (nullCount / (double) array.length) * 100;
        return new AnalysisReturnObject(nullPercentage, "rFloat"); // Return percentage of null values
    }

    // Method for Float array
    public AnalysisReturnObject func(Float[] array) {
        if (array == null || array.length == 0) {
            return new AnalysisReturnObject((float) 0.0, "rFloat"); // Return 0% if the array is empty or null
        }

        int nullCount = 0;
        for (Float num : array) {
            if (num == null) {
                nullCount++; // Increment the count for each null value
            }
        }

        Float nullPercentage = (float) (nullCount / (double) array.length) * 100;
        return new AnalysisReturnObject(nullPercentage, "rFloat"); // Return percentage of null values
    }

    // Method for String array
    public AnalysisReturnObject func(String[] array) {
        if (array == null || array.length == 0) {
            return new AnalysisReturnObject((float) 0.0, "rFloat"); // Return 0% if the array is empty or null
        }

        int nullCount = 0;
        for (String str : array) {
            if (str == null) {
                nullCount++; // Increment the count for each null value
            }
        }

        Float nullPercentage = (float) (nullCount / (double) array.length) * 100;
        return new AnalysisReturnObject(nullPercentage, "rFloat"); // Return percentage of null values
    }
}
