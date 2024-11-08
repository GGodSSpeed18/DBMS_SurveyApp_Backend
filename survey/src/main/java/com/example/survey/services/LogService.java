package com.example.survey.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.LogRepository;
import com.example.survey.data_transfer_objects.ActionLog;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    // ? Dont know if it works with external IP addresses yet.
    public String getLocationFromRequest(HttpServletRequest request) {
        // You can use request headers like "X-Forwarded-For" or integrate with a
        // geo-location API
        return request.getRemoteAddr(); // Example: using IP address
    }

    public void saveLog(ActionLog actionLog) {
        logRepository.saveLog(actionLog);
    }
}
