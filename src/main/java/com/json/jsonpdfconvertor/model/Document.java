package com.json.jsonpdfconvertor.model;

import lombok.Data;

import java.util.List;

@Data
public class Document {
    private String title;
    private String author;
    private String subject;
    private List<Section> sections;
    
    @Data
    public static class Section {
        private String heading;
        private String content;
    }
}