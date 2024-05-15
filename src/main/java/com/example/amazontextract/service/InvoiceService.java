package com.example.amazontextract.service;

import com.example.amazontextract.domain.PdfFile;
import com.example.amazontextract.service.process.ProcessPdfInvoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Value("${s3.bucketName}")
    private String path;

    private final ProcessPdfInvoice processPdfInvoice;
    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    public InvoiceService(ProcessPdfInvoice processPdfInvoice) {
        this.processPdfInvoice = processPdfInvoice;
    }

//    public List<Invoice> findAll() {
//        log.info("execute InvoiceService.findAll()");
//        return null;
//    }
//
//    public Optional<Invoice> findById(Long id) {
//        log.info("execute InvoiceService.findById(id)");
//        return null;
//    }

    public void processPdfFile(PdfFile pdfFile) {
        log.info("execute InvoiceService.processPdfFile(file)");
        pdfFile.setPdfFile(pdfFile.getPdfFile());
        processPdfInvoice.init(pdfFile);
    }
}
