package com.json.jsonpdfconvertor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

@SpringBootApplication
public class JsonPdfConvertorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonPdfConvertorApplication.class, args);
    }

}