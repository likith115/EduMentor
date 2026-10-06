package com.example.studyagent.service;

import org.springframework.stereotype.Component;

@Component
public class VectorUtils {

    public String toVectorString(float[] vector) {

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append(vector[i]);
        }
        result.append("]");
        return result.toString();
    }
}