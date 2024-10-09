package com.demo.epromissorias.controllers;

import com.demo.epromissorias.dao.PromissoriaDAO;
import com.demo.epromissorias.entities.Compra;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddCompraController {

    @FXML
    private TextField valorField;

    @FXML
    private DatePicker dataCompraPicker;

    private int promissoriaId; // ID da promissória para associar a nova compra

    // Método para salvar a nova compra
    @FXML
    public void salvarCompra() {
        String valorText = valorField.getText();
        LocalDate dataCompra = dataCompraPicker.getValue();

        if (valorText.isEmpty() || dataCompra == null) {
            // Exibir mensagem de erro (validação simples)
            System.out.println("Todos os campos devem ser preenchidos.");
            return;
        }

        double valor = Double.parseDouble(valorText); // Conversão de String para double

        Compra novaCompra = new Compra();
        novaCompra.setValor(valor);
        novaCompra.setData(dataCompra);

        PromissoriaDAO promissoriaDAO = new PromissoriaDAO();
        promissoriaDAO.addCompraToPromissoria(promissoriaId, novaCompra); // Chama o método para adicionar a compra

        // Fecha a janela atual
        Stage stage = (Stage) valorField.getScene().getWindow();
        stage.close();
    }

    // Método para cancelar e fechar a janela
    @FXML
    public void cancelar() {
        Stage stage = (Stage) valorField.getScene().getWindow();
        stage.close();
    }

    // Método para definir o ID da promissória ao inicializar a tela
    public void setPromissoriaId(int promissoriaId) {
        this.promissoriaId = promissoriaId;
    }
}
