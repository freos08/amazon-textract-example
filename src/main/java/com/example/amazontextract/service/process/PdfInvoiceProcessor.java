package com.example.amazontextract.service.process;

import com.example.amazontextract.domain.PdfFile;
import com.example.amazontextract.service.textract.common.AnalyzeExecutor;
import com.example.amazontextract.service.textract.common.AnalyzeExecutorFactory;
import com.example.amazontextract.service.textract.common.TextractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;

import java.util.ArrayList;
import java.util.List;

@Service
//@Transactional
public class PdfInvoiceProcessor {

    @Value("${s3.bucketName}")
    private String bucket;

    private static final String SUCCEEDED = "SUCCEEDED";

    private static final Logger log = LoggerFactory.getLogger(PdfInvoiceProcessor.class);

    private final AnalyzeExecutorFactory analyzeExecutorFactory;
    private final TextractService textractService;

    PdfInvoiceProcessor(AnalyzeExecutorFactory analyzeExecutorFactory,
                        TextractService textractService) {
        this.analyzeExecutorFactory = analyzeExecutorFactory;
        this.textractService = textractService;
    }

    //Starts the processing of the input document.
    public void processDocument(PdfFile pdfFile) {
        log.info("Start ProcessDocument");
        TextractClient textractClient = textractService.init(pdfFile);
        String startJobId = textractService.startDocumentAnalysis(textractClient, bucket, pdfFile.getDocument());

        if (startJobId != null) {

            log.info("Waiting for job: {}", startJobId);

            boolean jobFound = false;

            //loop until the job status is success.
            do {
                try {
                    String status = textractService.processDocument(startJobId, textractClient);
                    if (status.equals(SUCCEEDED)) {
                        log.info("Finish job id: {}", startJobId);
                        jobFound = true;
                        this.getDocumentAnalysisResults(textractClient, startJobId, pdfFile);
                    } else {
                        Thread.sleep(5000);
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    log.error("Error processing results", e);
                }
            } while (!jobFound);

            log.info("Finished processing document");
        }

    }

    //Gets the results of processing started by StartDocumentAnalysis
    private void getDocumentAnalysisResults(TextractClient textractClient, String startJobId, PdfFile pdfFile) {
        log.info("Start getting results: {}", startJobId);

        int maxResults = 10000;
        String paginationToken = null;
        boolean finished = false;
        List<Block> blocks = new ArrayList<>();

        //loops until pagination token is null
        while (!finished) {
            paginationToken =
                    textractService.getDocumentAnalysisResult(maxResults, startJobId, paginationToken, textractClient, blocks);
            if (paginationToken == null) {
                finished = true;
                log.info("Finished all pages document.");
                AnalyzeExecutor executor = null;
                try {
                    executor = analyzeExecutorFactory.getInstance(pdfFile.getTemplate());
                } catch (Exception e) {
                    log.error("Error getting executor", e);
                }
                if (executor != null) {
                    executor.init(blocks);
                }
            }
        }
    }
}
