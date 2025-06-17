package com.json.jsonpdfconvertor.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.jsonpdfconvertor.model.Document;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class JsonItemReader implements ItemReader<Document> {

    @Value("${input.json.file}")
    private Resource jsonResource;
    
    private boolean processed = false;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Document read() throws IOException {
        if (processed) {
            return null; // Return null to signal end of data
        }
        
        processed = true;
        return objectMapper.readValue(jsonResource.getInputStream(), Document.class);
    }
}