package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

import java.util.Arrays;

public class Median {

    // Method for integer array
    public AnalysisReturnObject func(Integer[] array) {
        Arrays.sort(array); // Sort the array

        int length = array.length;
        if (length % 2 == 0) {
            // If even, median is the average of the two middle numbers
            float median = (array[length / 2 - 1] + array[length / 2]) / 2;
            return new AnalysisReturnObject(median, "rFloat");
        } else {
            // If odd, median is the middle element
            float median = array[length / 2];
            return new AnalysisReturnObject(median, "rFloat");
        }
    }

    // Method for float array
    public AnalysisReturnObject func(Float[] array) {
        Arrays.sort(array); // Sort the array

        int length = array.length;
        if (length % 2 == 0) {
            // If even, median is the average of the two middle numbers
            float median = (array[length / 2 - 1] + array[length / 2]) / 2;
            return new AnalysisReturnObject(median, "rFloat");
        } else {
            // If odd, median is the middle element
            float median = array[length / 2];
            return new AnalysisReturnObject(median, "rFloat");
        }
    }

    public AnalysisReturnObject func(String[] args) {
        return new AnalysisReturnObject("", null);
    }
}
