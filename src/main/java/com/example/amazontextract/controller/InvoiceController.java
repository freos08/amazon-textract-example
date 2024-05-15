package com.example.amazontextract.controller;

import com.example.amazontextract.domain.PdfFile;
import com.example.amazontextract.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * POST  /invoices : process PDF file.
     *
     */
    @PostMapping
    public void createInvoice(@RequestBody PdfFile file) {
        log.info("REST request to process a pdf file");
        invoiceService.processPdfFile(file);
    }
}
