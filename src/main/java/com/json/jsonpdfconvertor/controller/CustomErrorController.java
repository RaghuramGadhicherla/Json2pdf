package com.json.jsonpdfconvertor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        
        String errorMessage;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (statusCode != null) {
            try {
                status = HttpStatus.valueOf(statusCode);
            } catch (Exception e) {
                // Keep default status if the code is not a standard HTTP status
            }
        }
        
        if (status == HttpStatus.NOT_FOUND) {
            errorMessage = "The requested page was not found";
        } else if (status == HttpStatus.METHOD_NOT_ALLOWED) {
            errorMessage = "The HTTP method used is not allowed for this endpoint. " +
                          "Please check the documentation for the correct method.";
        } else if (exception != null) {
            errorMessage = "An error occurred: " + exception.getMessage();
        } else {
            errorMessage = "An unexpected error occurred";
        }
        
        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }
}