package com.example.amazontextract.service.module.common;

import com.example.amazontextract.domain.TextractResult;
import com.example.amazontextract.service.process.IngestInvoicePdfFromTextract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.textract.model.Block;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AnalyzeExecutor {

    private final Logger log = LoggerFactory.getLogger(AnalyzeExecutor.class);

    private final IngestInvoicePdfFromTextract ingestInvoicePdfFromTextract;

    protected AnalyzeExecutor(IngestInvoicePdfFromTextract ingestInvoicePdfFromTextract) {
        this.ingestInvoicePdfFromTextract = ingestInvoicePdfFromTextract;
    }

    /**
     * Analyze textract result.
     */
    public void init(List<Block> blocks) {

        log.info("Start analyzing textract result of {} blocks", blocks.size());

        TextractResult textractResult = new TextractResult();
        List<Block> lineBlocks = blocks.stream().filter(block -> block.blockType().toString().equals("LINE")).collect(Collectors.toList());
        List<Block> cellBlocks = blocks.stream().filter(block -> block.blockType().toString().equals("CELL")).collect(Collectors.toList());
        List<Block> wordBlocks = blocks.stream().filter(block -> block.blockType().toString().equals("WORD")).collect(Collectors.toList());
        List<Block> tableBlocks = blocks.stream().filter(block -> block.blockType().toString().equals("TABLE")).collect(Collectors.toList());

        analyzeResult(textractResult, lineBlocks, cellBlocks, wordBlocks, tableBlocks);

        ingestInvoicePdfFromTextract.init(textractResult);

    }

    /**
     * Analyze textract result to generate headers, charges and equipments data.
     */
    protected abstract void analyzeResult(TextractResult textractResult, List<Block> lineBlocks, List<Block> cellBlocks, List<Block> wordBlocks, List<Block> tableBlocks);

}

