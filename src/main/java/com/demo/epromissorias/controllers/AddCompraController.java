package com.demo.epromissorias.controllers;

import com.demo.epromissorias.dao.PromissoriaDAO;
import com.demo.epromissorias.entities.Compra;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        // Verificação para campos vazios
        if (valorText.isEmpty() || dataCompra == null) {
            exibirAlerta("Erro", "Todos os campos devem ser preenchidos.");
            return;
        }

        try {
            double valor = Double.parseDouble(valorText); // Conversão de String para double

            Compra novaCompra = new Compra();
            novaCompra.setValor(valor);
            novaCompra.setData(dataCompra);

            PromissoriaDAO promissoriaDAO = new PromissoriaDAO();
            promissoriaDAO.addCompraToPromissoria(promissoriaId, novaCompra); // Chama o método para adicionar a compra

            // Fecha a janela atual
            Stage stage = (Stage) valorField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            exibirAlerta("Erro", "O valor inserido deve ser um número válido.");
        }
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

    // Método para exibir alertas
    private void exibirAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
