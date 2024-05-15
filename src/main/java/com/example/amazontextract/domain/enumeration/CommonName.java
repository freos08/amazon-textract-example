package com.example.amazontextract.domain.enumeration;

public enum CommonName {
    NIC("NIC"),
    SUMINISTRO("Id - Suministro"),
    NUMERO_FACTURA("FACTURA N°:"),
    TOTAL_A_PAGAR("TOTAL A PAGAR"),
    CONCEPTOS_ELECTRICOS("CONCEPTOS ELECTRICOS");

    private final String name;

    CommonName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
