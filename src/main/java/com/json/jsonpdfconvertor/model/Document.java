package com.json.jsonpdfconvertor.model;

import lombok.Data;

import java.util.List;

@Data
public class Document {
    private String title;
    private String author;
    private String subject;
    private List<Section> sections;
    
    // Explicit setters for compilation
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setSections(List<Section> sections) { this.sections = sections; }
    
    @Data
    public static class Section {
        private String heading;
        private String content;
        
        public void setHeading(String heading) { this.heading = heading; }
        public void setContent(String content) { this.content = content; }
    }
}