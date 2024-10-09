package com.demo.epromissorias.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void handleRegisterPromissory(ActionEvent event) {
        try {
            abrirCadastroPromissoria();
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível abrir a tela de cadastro: " + e.getMessage());
        }
    }

    private void abrirCadastroPromissoria() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/epromissorias/CadastroPromissoriaView.fxml"));
        Scene scene = new Scene(loader.load());
        Stage cadastroStage = new Stage();
        cadastroStage.setTitle("Cadastrar Promissória");
        cadastroStage.setScene(scene);
        cadastroStage.show();
    }

    @FXML
    private void handleViewPromissories(ActionEvent event) {
        try {
            abrirListagemPromissorias();
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível abrir a tela de visualização de promissórias: " + e.getMessage());

        }
    }

    private void abrirListagemPromissorias() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/epromissorias/ListagemPromissoriasView.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        Stage listagemStage = new Stage();
        listagemStage.setTitle("Visualizar Promissórias");
        listagemStage.setScene(scene);
        listagemStage.show();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // Fechar a janela atual
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setWidth(800);
        stage.setHeight(900);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
