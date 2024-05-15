package com.example.amazontextract.module.edemsa;

import com.example.amazontextract.domain.TextractResult;
import com.example.amazontextract.domain.enumeration.EdemsaKey;
import com.example.amazontextract.module.common.InvoiceAnalyzeExecutor;
import com.example.amazontextract.module.common.TextractTableGenerator;
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
public class EdemsaAnalyzeExecutor extends InvoiceAnalyzeExecutor {

    private final Logger log = LoggerFactory.getLogger(EdemsaAnalyzeExecutor.class);

    private final TextractTableGenerator textractTableGenerator;

    EdemsaAnalyzeExecutor(IngestInvoicePdfFromTextract ingestInvoicePdfFromTextract,
                          TextractTableGenerator textractTableGenerator) {
        super(ingestInvoicePdfFromTextract);
        this.textractTableGenerator = textractTableGenerator;
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
            String[][] tableMap = new String[0][0];
            List<Relationship> relationships = block.relationships();
            tableMap = textractTableGenerator.generateTableFromRelationship(relationships, cellBlocks, lineBlocks,
                    tableMap);

            if (EdemsaKey.TOTAL_A_PAGAR.getKey().equalsIgnoreCase(tableMap[0][0])) {
                // header table
                headerMap.put(EdemsaKey.TOTAL_A_PAGAR.getName(), tableMap[1][0]);
            }

            if (EdemsaKey.NUMERO_FACTURA.getKey().equalsIgnoreCase(tableMap[0][1])) {
                headerMap.put(EdemsaKey.NUMERO_FACTURA.getName(),
                        tableMap[0][1].split(EdemsaKey.NUMERO_FACTURA.getKey())[1]);
            }

            if (EdemsaKey.CONCEPTOS_ELECTRICOS.getKey().equalsIgnoreCase(tableMap[0][0]) && tableMap[0].length == 2) {
                chargeTable = tableMap;
            }

        }
        textractResult.setHeaderMap(headerMap);
        textractResult.setChargesTable(chargeTable);
    }

}
