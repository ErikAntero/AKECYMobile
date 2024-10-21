package com.fieb.akecy.model;

public class Cupom {
    private int idCupom;
    private String desconto;
    private String descricao;
    private String cashback;
    private String codigo;
    private String statusCupom;

    public int getIdCupom() {
        return idCupom;
    }

    public void setIdCupom(int idCupom) {
        this.idCupom = idCupom;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getStatusCupom() {
        return statusCupom;
    }
    public void setStatusCupom(String statusCupom) {
        this.statusCupom = statusCupom;
    }
}
