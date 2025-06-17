package com.json.jsonpdfconvertor.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/convert")
public class ConversionController {

    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job jsonToPdfJob;
    
    @PostMapping
    public ResponseEntity<String> convertJsonToPdf() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            JobExecution jobExecution = jobLauncher.run(jsonToPdfJob, jobParameters);
            return ResponseEntity.ok("Conversion job started successfully with ID: " + 
                                     jobExecution.getId() + " and status: " + 
                                     jobExecution.getStatus());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error starting conversion job: " + e.getMessage());
        }
    }
}