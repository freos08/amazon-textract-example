package com.example.amazontextract.domain;

public class PdfFile {

    private String pdfFile; // S3 path
    private boolean avoidDuplicates;
    private String template;

    public void PdfFile(String pdfFile, boolean avoidDuplicates) {
        this.pdfFile = pdfFile;
        this.avoidDuplicates = avoidDuplicates;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public boolean isAvoidDuplicates() {
        return avoidDuplicates;
    }

    public void setAvoidDuplicates(boolean avoidDuplicates) {
        this.avoidDuplicates = avoidDuplicates;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
