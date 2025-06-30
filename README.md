# JSON to PDF Converter

A Spring Boot application that uses Spring Batch and Apache PDFBox to convert JSON documents to PDF format.

## Features

- Converts structured JSON data to formatted PDF documents
- Reads JSON files from ZIP archives across multiple folder locations
- Supports document metadata (title, author, subject)
- Handles multiple sections with headings and content
- Provides REST API to trigger the conversion process
- Uses Spring Batch for efficient processing

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository
2. Build the project: `mvn clean install`
3. Run the application: `mvn spring-boot:run`

## Usage

### Input JSON Format

The application expects JSON files in the following format:

```json
{
  "title": "Document Title",
  "author": "Author Name",
  "subject": "Document Subject",
  "sections": [
    {
      "heading": "Section Heading",
      "content": "Section content goes here.\nUse \\n for line breaks."
    }
  ]
}
```

### API Endpoints

- `POST /api/convert`: Triggers the JSON to PDF conversion process

### Configuration

The following properties can be configured in `application.properties`:

- `input.folders`: Comma-separated list of folder paths to scan for ZIP files
- `output.pdf.directory`: Directory where PDF files will be saved

Example:
```properties
input.folders=./input,./data,c:/temp/json-files
```

## Implementation Details

- Spring Batch is used for the batch processing pipeline
- Apache PDFBox is used for PDF generation
- The conversion process is divided into three steps:
  1. Reading JSON data
  2. Processing the data (optional transformations)
  3. Writing the data to PDF format