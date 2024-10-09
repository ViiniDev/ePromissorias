package com.demo.epromissorias.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Promissoria {
    private int id;
    private String nome;
    private String endereco;
    private String cpf;
    private String telefone;
    private LocalDate dataCompra;
    private LocalDate dataPagamento;
    private double valorTotal;
    private List<Compra> compras; // Lista de compras

    public Promissoria() {
        this.compras = new ArrayList<>(); // Inicializa a lista de compras
    }


    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) { // Alterado para int
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public LocalDate getDataPagamento() { // Corrigido o nome do método
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) { // Corrigido o nome do método
        this.dataPagamento = dataPagamento;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void addCompra(Compra compra) {
        this.compras.add(compra);
        this.valorTotal += compra.getValor(); // Atualiza o valor total
    }

    // Override toString() to return only the name
    @Override
    public String toString() {
        return nome; // Return only the name
    }
}
