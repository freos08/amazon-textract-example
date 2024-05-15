package com.example.amazontextract.domain.enumeration;

public enum EdemsaKey {
    NIC("NIC", CommonName.NIC.getName()),
    SUMINISTRO("Id Suministro", CommonName.SUMINISTRO.getName()),
    NUMERO_FACTURA("FACTURA N°:", CommonName.NUMERO_FACTURA.getName()),
    TOTAL_A_PAGAR("TOTAL A PAGAR", CommonName.TOTAL_A_PAGAR.getName()),
    CONCEPTOS_ELECTRICOS("1. Conceptos Eléctricos", CommonName.CONCEPTOS_ELECTRICOS.getName());

    private final String key;
    private final String name;

    EdemsaKey(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
