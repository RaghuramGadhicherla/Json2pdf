package com.json.jsonpdfconvertor.batch;

import com.json.jsonpdfconvertor.model.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class JsonToPdfProcessor implements ItemProcessor<Document, Document> {

    @Override
    public Document process(Document document) {
        // You can perform any transformation on the document here if needed
        // For now, we'll just pass it through
        return document;
    }
}