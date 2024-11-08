package com.example.survey.analytics.functions;

import com.example.survey.analytics.AnalysisReturnObject;

public class StandardDev {

    // Method for Integer array
    public AnalysisReturnObject func(Integer[] array) {
        if (array == null || array.length == 0) {
            return new AnalysisReturnObject(0, "rFloat");
        }

        double sum = 0.0;
        double sumOfSquares = 0.0;
        int count = 0;

        for (Integer num : array) {
            if (num != null) { // Only consider non-null values
                sum += num;
                sumOfSquares += num * num;
                count++;
            }
        }

        if (count == 0) {
            return new AnalysisReturnObject(0, "rFloat");
        }

        double mean = sum / count;
        Float sdev = (float) Math.sqrt((sumOfSquares / count) - (mean * mean));
        return new AnalysisReturnObject(sdev, "rFloat");
    }

    // Method for Float array
    public AnalysisReturnObject func(Float[] array) {
        if (array == null || array.length == 0) {
            return new AnalysisReturnObject(0, "rFloat");
        }

        double sum = 0.0;
        double sumOfSquares = 0.0;
        int count = 0;

        for (Float num : array) {
            if (num != null) { // Only consider non-null values
                sum += num;
                sumOfSquares += num * num;
                count++;
            }
        }

        if (count == 0) {
            return new AnalysisReturnObject(0, "rFloat");
        }

        double mean = sum / count;
        Float sdev = (float) Math.sqrt((sumOfSquares / count) - (mean * mean));
        return new AnalysisReturnObject(sdev, "rFloat");
    }

    public AnalysisReturnObject func(String[] args) {
        return new AnalysisReturnObject("", null);
    }
}
