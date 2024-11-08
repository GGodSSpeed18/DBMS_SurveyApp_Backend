package com.example.survey.entities;

import java.io.Serializable;

public class DataType implements Serializable {
    private int type_id;
    private String type_name;
    private String type_desc;
    private Long max_val;
    private Long min_val;
    private int mappedTo;
    private boolean usesOptions;
    public static final int MAPPED_TO_INTEGER = 1;
    public static final int MAPPED_TO_FLOAT = 2;
    public static final int MAPPED_TO_STRING = 3;

    public DataType() {

    }

    public DataType(int type_id, String type_name, String type_desc) {
        this.type_id = type_id;
        this.type_name = type_name;
        this.type_desc = type_desc;
    }

    public Long getMax_val() {
        return max_val;
    }

    public void setMax_val(Long max_val) {
        this.max_val = max_val;
    }

    public Long getMin_val() {
        return min_val;
    }

    public void setMin_val(Long min_val) {
        this.min_val = min_val;
    }

    public int getMappedTo() {
        return mappedTo;
    }

    private String convertIntToEnum(int mappedToInt) {
        return switch (mappedToInt) {
            case MAPPED_TO_INTEGER -> "I";
            case MAPPED_TO_FLOAT -> "F";
            case MAPPED_TO_STRING -> "S";
            default -> "S";
        };
    }

    public String getMappedToString() {
        return convertIntToEnum(this.mappedTo);
    }

    public void setMappedTo(int mappedTo) {
        this.mappedTo = mappedTo;
    }

    private int convertEnumToInt(String mappedToEnum) {
        return switch (mappedToEnum) {
            case "I" -> MAPPED_TO_INTEGER;
            case "F" -> MAPPED_TO_FLOAT;
            case "S" -> MAPPED_TO_STRING;
            default -> MAPPED_TO_STRING;
        };
    }

    public void setMappedTo(String mappedToEnum) {
        this.mappedTo = convertEnumToInt(mappedToEnum);
    }

    public boolean isUsesOptions() {
        return usesOptions;
    }

    public void setUsesOptions(boolean usesOptions) {
        this.usesOptions = usesOptions;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

}
