package com.example.amazontextract.service.process;

import com.example.amazontextract.domain.Invoice;
import com.example.amazontextract.domain.TextractResult;
import com.example.amazontextract.domain.constants.EdemsaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
//@Transactional
public class IngestInvoicePdfFromTextract {

    private final Logger log = LoggerFactory.getLogger(IngestInvoicePdfFromTextract.class);

    public void init(TextractResult textractResult) {

        if (textractResult == null) {
            return;
        }

        log.debug("Start create invoice from result.");

        if (textractResult.getHeaderMap() != null) {

            Invoice invoice = new Invoice();
            invoice.setNic(textractResult.getHeaderMap().get(EdemsaConstants.NIC));
            invoice.setSuministro(textractResult.getHeaderMap().get(EdemsaConstants.SUMINISTRO));
            invoice.setNumeroFactura(textractResult.getHeaderMap().get(EdemsaConstants.NUMERO_FACTURA));
            try {
                String totalStr = textractResult.getHeaderMap().get(EdemsaConstants.TOTAL_A_PAGAR)
                        .replace("$", "")
                        .replace(".","")
                        .replace(",",".").trim();
                BigDecimal total = new BigDecimal(totalStr);
                invoice.setTotal(total);
            } catch (Exception e) {
                log.error("Error trying to parse total", e);
            }

            log.info("Invoice created {}", invoice);

            // We should create other entity for invoice charges. Meanwhile we only log the charges
            log.info("Charges of the invoice");
            String[][] chargesTable =  textractResult.getChargesTable();
            for (String[] strings : chargesTable) {
                String str = Arrays.toString(strings);
                log.info("ChargesTable {}", str);
            }
        }
    }
}
