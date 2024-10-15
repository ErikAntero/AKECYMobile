package com.fieb.akecy.model;

public class Produto {
    private int idProduto;
    private String nome;
    private String descricao;
    private String descricaoCompleta;
    private String tamanhosDisponiveis;
    private byte[] foto1;
    private byte[] foto2;
    private byte[] foto3;
    private byte[] foto4;
    private byte[] foto5;
    private String preco;
    private int idCategoria;
    private String StatusProd;

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao_completa() {
        return descricaoCompleta;
    }

    public void setDescricao_completa(String descricao_completa) {
        this.descricaoCompleta = descricao_completa;
    }

    public String getTamanhos_disponiveis() {
        return tamanhosDisponiveis;
    }

    public void setTamanhos_disponiveis(String tamanhos_disponiveis) {
        this.tamanhosDisponiveis = tamanhos_disponiveis;
    }

    public byte[] getFoto1() {
        return foto1;
    }

    public void setFoto1(byte[] foto1) {
        this.foto1 = foto1;
    }

    public byte[] getFoto2() {
        return foto2;
    }

    public void setFoto2(byte[] foto2) {
        this.foto2 = foto2;
    }

    public byte[] getFoto3() {
        return foto3;
    }

    public void setFoto3(byte[] foto3) {
        this.foto3 = foto3;
    }

    public byte[] getFoto4() {
        return foto4;
    }

    public void setFoto4(byte[] foto4) {
        this.foto4 = foto4;
    }

    public byte[] getFoto5() {
        return foto5;
    }

    public void setFoto5(byte[] foto5) {
        this.foto5 = foto5;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getStatusProd() {
        return StatusProd;
    }

    public void setStatusProd(String statusProd) {
        StatusProd = statusProd;
    }
}