package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

import java.util.HashSet;
import java.util.Set;

public class UniqueCount {

    // Method for integer array
    public AnalysisReturnObject func(int[] array) {
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : array) {
            uniqueSet.add(num); // Adds only unique values to the set
        }

        // Convert Set to List\
        Integer unique = uniqueSet.size();
        return new AnalysisReturnObject(unique, "rInteger");
    }

    // Method for float array
    public AnalysisReturnObject func(Float[] array) {
        Set<Float> uniqueSet = new HashSet<>();

        for (float num : array) {
            uniqueSet.add(num); // Adds only unique values to the set
        }

        // Convert Set to List
        Integer unique = uniqueSet.size();
        return new AnalysisReturnObject(unique, "rInteger");
    }

    // Method for string array
    public AnalysisReturnObject func(String[] array) {
        Set<String> uniqueSet = new HashSet<>();

        for (String str : array) {
            uniqueSet.add(str); // Adds only unique values to the set
        }

        // Convert Set to List
        Integer unique = uniqueSet.size();
        return new AnalysisReturnObject(unique, "rInteger");
    }
}
