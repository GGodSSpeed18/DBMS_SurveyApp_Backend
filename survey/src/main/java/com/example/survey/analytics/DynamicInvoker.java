package com.example.survey.analytics;

import java.lang.reflect.Method;
import java.util.List;

public class DynamicInvoker {
    private Object instance;
    private static final String BASE_PACKAGE = "com.example.survey.analytics.functions.";

    public DynamicInvoker(String className) throws Exception {
        // Prepend base package path if needed
        if (!className.startsWith(BASE_PACKAGE)) {
            className = BASE_PACKAGE + className;
        }

        // Load the class dynamically and create an instance
        Class<?> clazz = Class.forName(className);
        instance = clazz.getDeclaredConstructor().newInstance();
    }

    // public AnalysisReturnObject invokeFunc(Object array) throws Exception {
    public AnalysisReturnObject invokeFunc(List<?> list) throws Exception {
        // if (!(array instanceof Integer[] || array instanceof Float[] || array
        // instanceof String[])) {
        // throw new IllegalArgumentException("Array must be of type Integer[] or
        // Float[] or String[]");
        // }

        // // Determine method name based on array type
        // Method method;
        // if (array instanceof Integer[]) {
        // method = instance.getClass().getMethod("func", Integer[].class);
        // } else if (array instanceof Float[]) {
        // method = instance.getClass().getMethod("func", Float[].class);
        // } else if (array instanceof String[]) {
        // method = instance.getClass().getMethod("func", String[].class);
        // } else {
        // throw new IllegalArgumentException("Unsupported array type");
        // }

        // Convert List<?> to an array based on the type of elements in the list
        Object array;
        if (list.get(0) instanceof Integer) {
            array = list.toArray(new Integer[0]);
        } else if (list.get(0) instanceof Float) {
            array = list.toArray(new Float[0]);
        } else if (list.get(0) instanceof String) {
            array = list.toArray(new String[0]);
        } else {
            throw new IllegalArgumentException("Unsupported list element type");
        }

        // Determine method name based on array type
        Method method;
        if (array instanceof Integer[]) {
            method = instance.getClass().getMethod("func", Integer[].class);
        } else if (array instanceof Float[]) {
            method = instance.getClass().getMethod("func", Float[].class);
        } else if (array instanceof String[]) {
            method = instance.getClass().getMethod("func", String[].class);
        } else {
            throw new IllegalArgumentException("Unsupported array type");
        }

        // Invoke the appropriate method and return the result
        return (AnalysisReturnObject) method.invoke(instance, (Object) array); // Cast to Object to pass as a single
                                                                               // parameter
    }
}
