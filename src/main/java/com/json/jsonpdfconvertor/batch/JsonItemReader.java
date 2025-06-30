package com.json.jsonpdfconvertor.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.jsonpdfconvertor.config.InputConfig;
import com.json.jsonpdfconvertor.model.Document;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Iterator;

public class JsonItemReader implements ItemReader<Document> {

    @Autowired
    private InputConfig inputConfig;
    
    private List<Document> documents;
    private int currentIndex = 0;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private boolean initialized = false;

    @Override
    public Document read() throws IOException {
        if (!initialized) {
            loadAllDocuments();
            initialized = true;
        }
        
        if (currentIndex < documents.size()) {
            return documents.get(currentIndex++);
        }
        
        return null; // All documents processed
    }
    
    private void loadAllDocuments() throws IOException {
        documents = new ArrayList<>();
        List<File> zipFiles = findZipFiles();
        
        for (File zipFile : zipFiles) {
            try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(zipFile))) {
                ZipEntry entry;
                while ((entry = zipStream.getNextEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".json")) {
                        byte[] jsonBytes = zipStream.readAllBytes();
                        Document document = transformToDocument(jsonBytes);
                        if (document != null) {
                            documents.add(document);
                        }
                    }
                }
            }
        }
    }
    
    private List<File> findZipFiles() {
        List<File> zipFiles = new ArrayList<>();
        for (String folderPath : inputConfig.getFolders()) {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip"));
                if (files != null) {
                    for (File file : files) {
                        zipFiles.add(file);
                    }
                }
            }
        }
        return zipFiles;
    }
    
    private Document transformToDocument(byte[] jsonBytes) {
        try {
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(jsonBytes);
            Document document = new Document();
            
            // Dynamic title extraction
            String title = extractTitle(jsonNode);
            document.setTitle(title);
            document.setAuthor("System Generated");
            document.setSubject("JSON Data Report");
            
            // Dynamic content transformation
            List<Document.Section> sections = transformToSections(jsonNode);
            document.setSections(sections);
            return document;
        } catch (Exception e) {
            System.err.println("Error transforming JSON: " + e.getMessage());
            return null;
        }
    }
    
    private String extractTitle(com.fasterxml.jackson.databind.JsonNode jsonNode) {
        // Try common title fields
        String[] titleFields = {"title", "name", "company", "subject", "heading"};
        for (String field : titleFields) {
            if (jsonNode.has(field) && jsonNode.get(field).isTextual()) {
                return jsonNode.get(field).asText();
            }
        }
        return "JSON Document";
    }
    
    private List<Document.Section> transformToSections(com.fasterxml.jackson.databind.JsonNode jsonNode) {
        List<Document.Section> sections = new ArrayList<>();
        
        // Handle arrays (like employees, items, etc.)
        jsonNode.fields().forEachRemaining(entry -> {
            if (entry.getValue().isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode item : entry.getValue()) {
                    Document.Section section = new Document.Section();
                    section.setHeading(extractItemHeading(item, entry.getKey()));
                    section.setContent(formatJsonContent(item));
                    sections.add(section);
                }
            } else if (entry.getValue().isObject()) {
                Document.Section section = new Document.Section();
                section.setHeading(entry.getKey());
                section.setContent(formatJsonContent(entry.getValue()));
                sections.add(section);
            }
        });
        
        // If no sections created, create one with all data
        if (sections.isEmpty()) {
            Document.Section section = new Document.Section();
            section.setHeading("Content");
            section.setContent(formatJsonContent(jsonNode));
            sections.add(section);
        }
        
        return sections;
    }
    
    private String extractItemHeading(com.fasterxml.jackson.databind.JsonNode item, String arrayName) {
        String[] headingFields = {"name", "title", "id", "key"};
        for (String field : headingFields) {
            if (item.has(field)) {
                return item.get(field).asText();
            }
        }
        return arrayName + " Item";
    }
    
    private String formatJsonContent(com.fasterxml.jackson.databind.JsonNode node) {
        StringBuilder content = new StringBuilder();
        node.fields().forEachRemaining(entry -> {
            if (!entry.getValue().isArray() && !entry.getValue().isObject()) {
                content.append(entry.getKey()).append(": ").append(entry.getValue().asText()).append("\n");
            }
        });
        return content.toString().trim();
    }
}