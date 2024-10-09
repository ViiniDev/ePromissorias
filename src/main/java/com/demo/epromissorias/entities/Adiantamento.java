package com.demo.epromissorias.entities;

import java.time.LocalDate;

public class Adiantamento {
    private int id;
    private double valor;
    private LocalDate data;

    public Adiantamento(int id, double valor, LocalDate data) {
        this.id = id;
        this.valor = valor;
        this.data = data;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

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
