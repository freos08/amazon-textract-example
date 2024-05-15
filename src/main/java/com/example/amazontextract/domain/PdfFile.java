package com.example.amazontextract.domain;

public class PdfFile {

    private String document; // S3 document path
    private String template;

    public void PdfFile(String document) {
        this.document = document;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
