package com.example.amazontextract.service.textract.edemsa;

import com.example.amazontextract.domain.TextractResult;
import com.example.amazontextract.domain.constants.EdemsaConstants;
import com.example.amazontextract.service.process.IngestInvoicePdfFromTextract;
import com.example.amazontextract.service.textract.common.AnalyzeExecutor;
import com.example.amazontextract.service.textract.util.TextractTableGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.Relationship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@Transactional
public class EdemsaAnalyzeExecutor extends AnalyzeExecutor {

    private final Logger log = LoggerFactory.getLogger(EdemsaAnalyzeExecutor.class);

    EdemsaAnalyzeExecutor(IngestInvoicePdfFromTextract ingestInvoicePdfFromTextract) {
        super(ingestInvoicePdfFromTextract);
    }

    @Override
    public void analyzeResult(TextractResult textractResult, List<Block> lineBlocks, List<Block> cellBlocks, List<Block> wordBlocks, List<Block> tableBlocks) {

        log.debug("Start analyze result for Edemsa template");
        Map<String, String> headerMap = new HashMap<>();
        String[][] chargeTable = null;

        for (int x = 0; x < lineBlocks.size(); x ++) {
            Block block = lineBlocks.get(x);
            if (block.text().equals(EdemsaConstants.NIC)) {
                headerMap.put(EdemsaConstants.NIC, lineBlocks.get(x + 1).text());
            }
            if (block.text().equals(EdemsaConstants.SUMINISTRO)) {
                headerMap.put(EdemsaConstants.SUMINISTRO, lineBlocks.get(x + 1).text());
            }
        }

        //iterating over Tables
        for (Block block : tableBlocks) {
            List<Relationship> relationships = block.relationships();
            String[][] tableMap = TextractTableGenerator.generateTableFromRelationship(relationships, cellBlocks, lineBlocks);

            if (EdemsaConstants.TOTAL_A_PAGAR.equalsIgnoreCase(tableMap[0][0])) {
                // header table
                headerMap.put(EdemsaConstants.TOTAL_A_PAGAR, tableMap[1][0]);
            }

            if (tableMap[0][0] != null && tableMap[0][0].startsWith(EdemsaConstants.NUMERO_FACTURA)) {
                headerMap.put(EdemsaConstants.NUMERO_FACTURA,
                        tableMap[0][0].split(EdemsaConstants.NUMERO_FACTURA)[1]);
            }

            if (EdemsaConstants.CONCEPTOS_ELECTRICOS.equalsIgnoreCase(tableMap[0][0]) && tableMap[0].length == 2) {
                chargeTable = tableMap;
            }
        }
        textractResult.setHeaderMap(headerMap);
        textractResult.setChargesTable(chargeTable);
    }

}
