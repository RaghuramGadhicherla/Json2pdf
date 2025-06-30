package com.json.jsonpdfconvertor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "input")
public class InputConfig {
    
    private List<String> folders;
    
    public List<String> getFolders() {
        return folders;
    }
    
    public void setFolders(List<String> folders) {
        this.folders = folders;
    }
}