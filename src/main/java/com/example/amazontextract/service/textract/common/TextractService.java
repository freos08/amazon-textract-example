package com.example.amazontextract.service.textract.common;

import com.example.amazontextract.domain.PdfFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.DocumentLocation;
import software.amazon.awssdk.services.textract.model.FeatureType;
import software.amazon.awssdk.services.textract.model.GetDocumentAnalysisRequest;
import software.amazon.awssdk.services.textract.model.GetDocumentAnalysisResponse;
import software.amazon.awssdk.services.textract.model.S3Object;
import software.amazon.awssdk.services.textract.model.StartDocumentAnalysisRequest;
import software.amazon.awssdk.services.textract.model.StartDocumentAnalysisResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextractService {

    private static final Logger log = LoggerFactory.getLogger(TextractService.class);

    public TextractClient init(PdfFile pdfFile) {
        log.info("Starting PDF process with textract for {} ", pdfFile.getDocument());
        return TextractClient.builder()
            .region(Region.US_WEST_2)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();
    }

    public String startDocumentAnalysis(TextractClient textractClient, String bucket, String document) {
        log.info("Start Document Analysis");

        List<FeatureType> myList = new ArrayList<>();
        myList.add(FeatureType.TABLES);
        myList.add(FeatureType.FORMS);

        S3Object s3Object = S3Object.builder()
                .bucket(bucket)
                .name(document)
                .build();

        DocumentLocation location = DocumentLocation.builder()
                .s3Object(s3Object)
                .build();

        StartDocumentAnalysisRequest req = StartDocumentAnalysisRequest.builder()
                .documentLocation(location)
                .featureTypes(myList)
                .build();

        StartDocumentAnalysisResponse startDocumentAnalysisResult = textractClient.startDocumentAnalysis(req);

        if (startDocumentAnalysisResult != null) {
            return startDocumentAnalysisResult.jobId();
        } else {
            return null;
        }
    }

    public String processDocument(String startJobId, TextractClient textractClient) {
        GetDocumentAnalysisRequest analysisRequest = GetDocumentAnalysisRequest.builder()
            .jobId(startJobId)
            .maxResults(10000)
            .build();

        GetDocumentAnalysisResponse response = textractClient.getDocumentAnalysis(analysisRequest);
        log.debug("Status of jobId {} is {}", startJobId, response.jobStatus());
        return response.jobStatus().toString();
    }

    public String getDocumentAnalysisResult(int maxResults, String startJobId, String paginationToken, TextractClient textractClient, List<Block> blocks) {
        GetDocumentAnalysisRequest documentAnalysisRequest = GetDocumentAnalysisRequest.builder()
            .jobId(startJobId)
            .maxResults(maxResults)
            .nextToken(paginationToken)
            .build();

        GetDocumentAnalysisResponse response = textractClient.getDocumentAnalysis(documentAnalysisRequest);

        //Show blocks, confidence and detection times
        log.info("Adding {} blocks to result", blocks.size());
        blocks.addAll(response.blocks());

        paginationToken = response.nextToken();
        return paginationToken;
    }
}
