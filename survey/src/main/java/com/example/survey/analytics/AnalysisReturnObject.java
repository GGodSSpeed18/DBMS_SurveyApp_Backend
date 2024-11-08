package com.example.survey.analytics;

import java.util.List;
import java.util.Map;

public class AnalysisReturnObject {
    private Integer rInteger;
    private String rString;
    private Float rFloat;
    private List<Integer> lInteger;
    private List<Float> lFloat;
    private List<String> lString;
    private Map<Integer, Integer> mInteger;
    private Map<Float, Integer> mFloat;
    private Map<String, Integer> mString;

    private String uses;
    private String name;

    // Map<Integer, Integer> map = new HashMap<>();
    // map.put(1, 3);
    // map.put(5, 6);
    // map.put(7, 10);
    // AnalysisReturnObject analysisReturnObject = new AnalysisReturnObject(map,
    // "mInteger");
    @SuppressWarnings("unchecked")
    public AnalysisReturnObject(Map<?, ?> map, String uses) {
        try {
            switch (uses) {
                case "mInteger":
                    if (map.keySet().stream().allMatch(k -> k instanceof Integer) &&
                            map.values().stream().allMatch(v -> v instanceof Integer)) {
                        mInteger = (Map<Integer, Integer>) map;
                    } else {
                        throw new IllegalArgumentException("Map key/value types do not match Integer/Integer.");
                    }
                    break;

                case "mFloat":
                    if (map.keySet().stream().allMatch(k -> k instanceof Float) &&
                            map.values().stream().allMatch(v -> v instanceof Integer)) {
                        mFloat = (Map<Float, Integer>) map;
                    } else {
                        throw new IllegalArgumentException("Map key/value types do not match Float/Integer.");
                    }
                    break;

                case "mString":
                    if (map.keySet().stream().allMatch(k -> k instanceof String) &&
                            map.values().stream().allMatch(v -> v instanceof Integer)) {
                        mString = (Map<String, Integer>) map;
                    } else {
                        throw new IllegalArgumentException("Map key/value types do not match String/Integer.");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Mismatched uses string and map dtype.");
            }
            this.uses = uses;
        } catch (Exception e) {
            throw new IllegalArgumentException("Mismatched uses string and map dtype.");
        }
    }

    // AnalysisReturnObject analysisReturnObject = new AnalysisReturnObject(new
    // ArrayList<>(Arrays.asList(a)),
    // "lFloat");
    @SuppressWarnings("unchecked")
    public AnalysisReturnObject(List<?> list, String uses) {
        try {
            // Verify list's element types based on 'uses' string
            switch (uses) {
                case "lInteger":
                    if (list.stream().allMatch(e -> e instanceof Integer)) {
                        lInteger = (List<Integer>) list;
                    } else {
                        throw new IllegalArgumentException("List elements do not match Integer.");
                    }
                    break;

                case "lFloat":
                    if (list.stream().allMatch(e -> e instanceof Float)) {
                        lFloat = (List<Float>) list;
                    } else {
                        throw new IllegalArgumentException("List elements do not match Float.");
                    }
                    break;

                case "lString":
                    if (list.stream().allMatch(e -> e instanceof String)) {
                        lString = (List<String>) list;
                    } else {
                        throw new IllegalArgumentException("List elements do not match String.");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Mismatched uses string and list dtype.");
            }

            this.uses = uses;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating AnalysisReturnObject: " + e.getMessage(), e);
        }
    }

    public AnalysisReturnObject(Float rFloat, String uses) {
        this.rFloat = rFloat;
        // this.uses = uses;
        this.uses = "rFloat";
    }

    public AnalysisReturnObject(String rString, String uses) {
        this.rString = rString;
        // this.uses = uses;
        this.uses = "rString";
    }

    public AnalysisReturnObject(Integer rInteger, String uses) {
        this.rInteger = rInteger;
        // this.uses = uses;
        this.uses = "rInteger";
    }

    public Integer getrInteger() {
        return rInteger;
    }

    public void setrInteger(Integer rInteger) {
        this.rInteger = rInteger;
    }

    public String getrString() {
        return rString;
    }

    public void setrString(String rString) {
        this.rString = rString;
    }

    public Float getrFloat() {
        return rFloat;
    }

    public void setrFloat(Float rFloat) {
        this.rFloat = rFloat;
    }

    public List<Integer> getlInteger() {
        return lInteger;
    }

    public void setlInteger(List<Integer> lInteger) {
        this.lInteger = lInteger;
    }

    public List<Float> getlFloat() {
        return lFloat;
    }

    public void setlFloat(List<Float> lFloat) {
        this.lFloat = lFloat;
    }

    public List<String> getlString() {
        return lString;
    }

    public void setlString(List<String> lString) {
        this.lString = lString;
    }

    public Map<Integer, Integer> getmInteger() {
        return mInteger;
    }

    public void setmInteger(Map<Integer, Integer> mInteger) {
        this.mInteger = mInteger;
    }

    public Map<Float, Integer> getmFloat() {
        return mFloat;
    }

    public void setmFloat(Map<Float, Integer> mFloat) {
        this.mFloat = mFloat;
    }

    public Map<String, Integer> getmString() {
        return mString;
    }

    public void setmString(Map<String, Integer> mString) {
        this.mString = mString;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
