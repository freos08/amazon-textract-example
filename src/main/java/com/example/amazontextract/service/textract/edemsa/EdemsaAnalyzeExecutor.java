package com.example.amazontextract.service.textract.edemsa;

import com.example.amazontextract.domain.TextractResult;
import com.example.amazontextract.domain.enumeration.EdemsaKey;
import com.example.amazontextract.service.textract.common.AnalyzeExecutor;
import com.example.amazontextract.service.textract.util.TextractTableGenerator;
import com.example.amazontextract.service.process.IngestInvoicePdfFromTextract;
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
            if (block.text().equals(EdemsaKey.NIC.getKey())) {
                headerMap.put(EdemsaKey.NIC.getName(), lineBlocks.get(x + 1).text());
            }
            if (block.text().equals(EdemsaKey.SUMINISTRO.getKey())) {
                headerMap.put(EdemsaKey.SUMINISTRO.getName(), lineBlocks.get(x + 1).text());
            }
        }

        //iterating over Tables
        for (Block block : tableBlocks) {
            List<Relationship> relationships = block.relationships();
            String[][] tableMap = TextractTableGenerator.generateTableFromRelationship(relationships, cellBlocks, lineBlocks);

            if (EdemsaKey.TOTAL_A_PAGAR.getKey().equalsIgnoreCase(tableMap[0][0])) {
                // header table
                headerMap.put(EdemsaKey.TOTAL_A_PAGAR.getName(), tableMap[1][0]);
            }

            if (tableMap[0][0] != null && tableMap[0][0].startsWith(EdemsaKey.NUMERO_FACTURA.getKey())) {
                headerMap.put(EdemsaKey.NUMERO_FACTURA.getName(),
                        tableMap[0][0].split(EdemsaKey.NUMERO_FACTURA.getKey())[1]);
            }

            if (EdemsaKey.CONCEPTOS_ELECTRICOS.getKey().equalsIgnoreCase(tableMap[0][0]) && tableMap[0].length == 2) {
                chargeTable = tableMap;
            }
        }
        textractResult.setHeaderMap(headerMap);
        textractResult.setChargesTable(chargeTable);
    }

}
