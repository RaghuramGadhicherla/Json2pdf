package com.json.jsonpdfconvertor.config;

import com.json.jsonpdfconvertor.batch.JsonItemReader;
import com.json.jsonpdfconvertor.batch.PdfItemWriter;
import com.json.jsonpdfconvertor.batch.JsonToPdfProcessor;
import com.json.jsonpdfconvertor.model.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public JsonItemReader jsonItemReader() {
        return new JsonItemReader();
    }

    @Bean
    public JsonToPdfProcessor jsonToPdfProcessor() {
        return new JsonToPdfProcessor();
    }

    @Bean
    public PdfItemWriter pdfItemWriter() {
        return new PdfItemWriter();
    }

    @Bean
    public Step jsonToPdfStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jsonToPdfStep", jobRepository)
                .<Document, Document>chunk(1, transactionManager)
                .reader(jsonItemReader())
                .processor(jsonToPdfProcessor())
                .writer(pdfItemWriter())
                .build();
    }

    @Bean
    public Job jsonToPdfJob(JobRepository jobRepository, Step jsonToPdfStep) {
        return new JobBuilder("jsonToPdfJob", jobRepository)
                .start(jsonToPdfStep)
                .build();
    }
}