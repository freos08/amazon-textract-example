package com.example.amazontextract.service;

import com.example.amazontextract.domain.PdfFile;
import com.example.amazontextract.service.process.PdfInvoiceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final PdfInvoiceProcessor pdfInvoiceProcessor;
    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    public InvoiceService(PdfInvoiceProcessor pdfInvoiceProcessor) {
        this.pdfInvoiceProcessor = pdfInvoiceProcessor;
    }

    public void processPdfFile(PdfFile pdfFile) {
        log.info("execute InvoiceService.processPdfFile(file)");
        pdfInvoiceProcessor.processDocument(pdfFile);
    }
}
