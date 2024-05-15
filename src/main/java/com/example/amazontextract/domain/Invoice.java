package com.example.amazontextract.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * An Invoice.
 */
public class Invoice implements Serializable {

    private Long id;
    private String nic;
    private String suministro;
    private String numeroFactura;
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getSuministro() {
        return suministro;
    }

    public void setSuministro(String suministro) {
        this.suministro = suministro;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) && Objects.equals(nic, invoice.nic) && Objects.equals(suministro,
                invoice.suministro) && Objects.equals(numeroFactura, invoice.numeroFactura) && Objects.equals(total,
                invoice.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nic, suministro, numeroFactura, total);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", nic='" + nic + '\'' +
                ", suministro='" + suministro + '\'' +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
