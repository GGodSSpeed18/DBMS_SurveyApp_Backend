package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

import java.util.HashMap;
import java.util.Map;

public class Mode {

    // Method for integer array
    public AnalysisReturnObject func(Integer[] array) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int num : array) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int maxCount = 0;
        Integer mode = 0;

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        return new AnalysisReturnObject(mode, "rInteger");
    }

    // Method for float array
    public AnalysisReturnObject func(Float[] array) {
        Map<Float, Integer> frequencyMap = new HashMap<>();

        for (float num : array) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int maxCount = 0;
        Float mode = null;

        for (Map.Entry<Float, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        return new AnalysisReturnObject(mode, "rFloat");
    }

    public AnalysisReturnObject func(String[] args) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Count occurrences of each string
        for (String str : args) {
            frequencyMap.put(str, frequencyMap.getOrDefault(str, 0) + 1);
        }

        int maxCount = 0;
        String mode = null;

        // Find the string with the highest frequency
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        return new AnalysisReturnObject(mode, "rString");
    }
}
