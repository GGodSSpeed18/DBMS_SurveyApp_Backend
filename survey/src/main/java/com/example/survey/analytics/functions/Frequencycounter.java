package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

import java.util.HashMap;
import java.util.Map;

public class Frequencycounter {

    // Method for integer array
    public AnalysisReturnObject func(Integer[] array) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int num : array) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        return new AnalysisReturnObject(frequencyMap, "mInteger");
    }

    // Method for float array
    public AnalysisReturnObject func(Float[] array) {
        Map<Float, Integer> frequencyMap = new HashMap<>();

        for (float num : array) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        return new AnalysisReturnObject(frequencyMap, "mFloat");
    }

    // Method for string array
    public AnalysisReturnObject func(String[] array) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String str : array) {
            frequencyMap.put(str, frequencyMap.getOrDefault(str, 0) + 1);
        }

        return new AnalysisReturnObject(frequencyMap, "mString");
    }
}
