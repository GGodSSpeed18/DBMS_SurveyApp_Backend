package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

public class Mean {
    public AnalysisReturnObject func(Integer[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        float sum = 0;
        int nonNullCount = 0;

        for (Integer arg : args) {
            if (arg != null) {
                sum += arg;
                nonNullCount++;
            }
        }

        if (nonNullCount == 0) { // All values are null
            return null;
        }

        return new AnalysisReturnObject(sum / nonNullCount, "rFloat");
    }

    public AnalysisReturnObject func(Float[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        float sum = 0;
        int nonNullCount = 0;

        for (Float arg : args) {
            if (arg != null) {
                sum += arg;
                nonNullCount++;
            }
        }

        if (nonNullCount == 0) { // All values are null
            return null;
        }

        return new AnalysisReturnObject(sum / nonNullCount, "rFloat");
    }

    public AnalysisReturnObject func(String[] args) {
        return new AnalysisReturnObject("", null);
    }
}
