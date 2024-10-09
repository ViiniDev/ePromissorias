package com.demo.epromissorias.entities;

import java.time.LocalDate;

public class Compra {
    private int id; // Adicione o campo id
    private double valor;
    private LocalDate data; // Campo data, se necessário

    public Compra() {
        // Inicializando os campos, se necessário
    }

    // Adiciona o construtor para inicializar o id
    public Compra(int id, double valor, LocalDate data) {
        this.id = id;
        this.valor = valor;
        this.data = data;
    }

    // Método para obter o id
    public int getId() {
        return id;
    }

    // Método para definir o id
    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
