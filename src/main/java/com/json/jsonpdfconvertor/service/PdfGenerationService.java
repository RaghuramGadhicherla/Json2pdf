package com.json.jsonpdfconvertor.service;

import com.json.jsonpdfconvertor.model.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfGenerationService {

    public void generatePdf(Document document, String outputPath) throws IOException {
        try (PDDocument pdfDocument = new PDDocument()) {
            // Set document metadata
            pdfDocument.getDocumentInformation().setTitle(document.getTitle());
            pdfDocument.getDocumentInformation().setAuthor(document.getAuthor());
            pdfDocument.getDocumentInformation().setSubject(document.getSubject());
            
            // Create title page
            createTitlePage(pdfDocument, document);
            
            // Create content pages
            if (document.getSections() != null) {
                for (Document.Section section : document.getSections()) {
                    createContentPage(pdfDocument, section);
                }
            }
            
            // Save the PDF
            pdfDocument.save(outputPath);
        }
    }
    
    private void createTitlePage(PDDocument pdfDocument, Document document) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        pdfDocument.addPage(page);
        
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(document.getTitle());
            contentStream.endText();
            
            // Author
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 16);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("Author: " + document.getAuthor());
            contentStream.endText();
            
            // Subject
            if (document.getSubject() != null && !document.getSubject().isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 14);
                contentStream.newLineAtOffset(100, 620);
                contentStream.showText("Subject: " + document.getSubject());
                contentStream.endText();
            }
        }
    }
    
    private void createContentPage(PDDocument pdfDocument, Document.Section section) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        pdfDocument.addPage(page);
        
        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
            // Section heading
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText(section.getHeading());
            contentStream.endText();
            
            // Section content
            if (section.getContent() != null && !section.getContent().isEmpty()) {
                String[] paragraphs = section.getContent().split("\n");
                float y = 720;
                
                for (String paragraph : paragraphs) {
                    // Simple text wrapping
                    String[] words = paragraph.split(" ");
                    StringBuilder line = new StringBuilder();
                    
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, y);
                    
                    for (String word : words) {
                        if (line.length() + word.length() + 1 > 80) { // Simple line length limit
                            contentStream.showText(line.toString());
                            contentStream.newLineAtOffset(0, -15);
                            y -= 15;
                            line = new StringBuilder();
                        }
                        
                        if (line.length() > 0) {
                            line.append(" ");
                        }
                        line.append(word);
                    }
                    
                    if (line.length() > 0) {
                        contentStream.showText(line.toString());
                    }
                    
                    contentStream.endText();
                    y -= 30; // Space between paragraphs
                    
                    if (y < 50) {
                        // Create a new page if we're running out of space
                        page = new PDPage(PDRectangle.A4);
                        pdfDocument.addPage(page);
                        contentStream.close();
                        try (PDPageContentStream newContentStream = new PDPageContentStream(pdfDocument, page)) {
                            y = 750;
                        }
                    }
                }
            }
        }
    }
}