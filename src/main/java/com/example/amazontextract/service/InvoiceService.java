package com.example.amazontextract.service;

import com.example.amazontextract.domain.PdfFile;
import com.example.amazontextract.service.process.ProcessPdfInvoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final ProcessPdfInvoice processPdfInvoice;
    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    public InvoiceService(ProcessPdfInvoice processPdfInvoice) {
        this.processPdfInvoice = processPdfInvoice;
    }

    public void processPdfFile(PdfFile pdfFile) {
        log.info("execute InvoiceService.processPdfFile(file)");
        processPdfInvoice.init(pdfFile);
    }
}
