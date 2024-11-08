package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

public class Total {

    // Method for Integer array
    public AnalysisReturnObject func(Integer[] array) {
        int total = 0;

        for (Integer num : array) {
            if (num != null) { // Only add non-null values
                total += num;
            }
        }

        return new AnalysisReturnObject(total, "rInteger");
    }

    // Method for Float array
    public AnalysisReturnObject func(Float[] array) {
        float total = 0.0f;

        for (Float num : array) {
            if (num != null) { // Only add non-null values
                total += num;
            }
        }

        return new AnalysisReturnObject(total, "rInteger");
    }

    public AnalysisReturnObject func(String[] args) {
        return new AnalysisReturnObject("", null);
    }
}
