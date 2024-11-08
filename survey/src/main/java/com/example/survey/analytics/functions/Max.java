package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

public class Max {

    // Method for integer array
    public AnalysisReturnObject func(Integer[] array) {
        if (array == null || array.length == 0) {
            return null; // Return null if array is empty or null
        }

        int max = array[0];
        for (int num : array) {
            if (num > max) {
                max = num;
            }
        }
        return new AnalysisReturnObject(max, "rInteger");
    }

    // Method for float array
    public AnalysisReturnObject func(Float[] array) {
        if (array == null || array.length == 0) {
            return null; // Return null if array is empty or null
        }

        float max = array[0];
        for (float num : array) {
            if (num > max) {
                max = num;
            }
        }
        return new AnalysisReturnObject(max, "rFloat");
    }

    public AnalysisReturnObject func(String[] args) {
        return new AnalysisReturnObject("", null);
    }
}
