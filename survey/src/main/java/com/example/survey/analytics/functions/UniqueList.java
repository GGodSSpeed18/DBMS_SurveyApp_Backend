package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueList {

    // Method for integer array
    public AnalysisReturnObject func(Integer[] array) {
        Set<Integer> uniqueSet = new HashSet<>();

        for (int num : array) {
            uniqueSet.add(num); // Adds only unique values to the set
        }

        // Convert Set to List\
        List<Integer> uniqueList = new ArrayList<>(uniqueSet);
        return new AnalysisReturnObject(uniqueList, "lInteger");
    }

    // Method for float array
    public AnalysisReturnObject func(Float[] array) {
        Set<Float> uniqueSet = new HashSet<>();

        for (float num : array) {
            uniqueSet.add(num); // Adds only unique values to the set
        }

        // Convert Set to List
        List<Float> uniqueList = new ArrayList<>(uniqueSet);
        return new AnalysisReturnObject(uniqueList, "lFloat");
    }

    // Method for string array
    public AnalysisReturnObject func(String[] array) {
        Set<String> uniqueSet = new HashSet<>();

        for (String str : array) {
            uniqueSet.add(str); // Adds only unique values to the set
        }

        // Convert Set to List
        List<String> uniqueList = new ArrayList<>(uniqueSet);
        return new AnalysisReturnObject(uniqueList, "lString");
    }
}
