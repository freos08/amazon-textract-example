package com.example.amazontextract.domain;

import java.util.Map;

public class TextractResult {

    private Map<String, String> headerMap;
    private String[][] chargesTable;

    public void TextractResult(Map<String, String> headerMap, String[][] chargesTable) {
        this.headerMap = headerMap;
        this.chargesTable = chargesTable;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String[][] getChargesTable() {
        return chargesTable;
    }

    public void setChargesTable(String[][] chargesTable) {
        this.chargesTable = chargesTable;
    }

}
