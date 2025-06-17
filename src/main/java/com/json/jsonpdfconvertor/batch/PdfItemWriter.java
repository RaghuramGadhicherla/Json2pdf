package com.json.jsonpdfconvertor.batch;

import com.json.jsonpdfconvertor.model.Document;
import com.json.jsonpdfconvertor.service.PdfGenerationService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfItemWriter implements ItemWriter<Document> {

    @Autowired
    private PdfGenerationService pdfGenerationService;
    
    @Value("${output.pdf.directory}")
    private String outputDirectory;

    @Override
    public void write(Chunk<? extends Document> chunk) throws Exception {
        for (Document document : chunk) {
            String filename = document.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
            Path outputPath = Paths.get(outputDirectory, filename);
            pdfGenerationService.generatePdf(document, outputPath.toString());
        }
    }
}