package com.demo.epromissorias.controllers;

import com.demo.epromissorias.dao.PromissoriaDAO;
import com.demo.epromissorias.entities.Promissoria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListagemPromissoriasController {
    @FXML
    private ListView<Promissoria> listView;

    @FXML
    private TextField searchField;  // Para capturar o campo de pesquisa

    private PromissoriaDAO promissoriaDAO;

    public ListagemPromissoriasController() {
        this.promissoriaDAO = new PromissoriaDAO();
    }

    @FXML
    public void initialize() {
        // Carregar todas as promissórias quando a tela é aberta
        atualizarListaCompleta();
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verifica se o clique foi duplo
                Promissoria selectedPromissoria = listView.getSelectionModel().getSelectedItem();
                if (selectedPromissoria != null) {
                    openDetailsScreen(selectedPromissoria.getId()); // Passa o ID
                }
            }
        });
    }

    // Método para atualizar a lista com todas as promissórias
    private void atualizarListaCompleta() {
        try {
            List<Promissoria> promissorias = promissoriaDAO.findAll(); // Método para buscar todas as promissórias
            listView.getItems().clear();
            listView.getItems().addAll(promissorias);
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível carregar as promissórias: " + e.getMessage());
        }
    }

    // Método para atualizar a lista com as promissórias de acordo com a pesquisa
    @FXML  // Adicionar a anotação @FXML
    public void atualizarLista() {
        String searchTerm = searchField.getText().toLowerCase();  // Captura o texto da pesquisa
        try {
            List<Promissoria> promissorias = promissoriaDAO.findPromissoriaByName(searchTerm); // Usar o texto para buscar
            listView.getItems().clear();
            listView.getItems().addAll(promissorias);

            if (promissorias.isEmpty()) {
                showAlert("Aviso", "Nenhuma promissória encontrada para o termo de pesquisa: " + searchTerm);
            }
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível carregar as promissórias: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openDetailsScreen(int promissoriaId) {
        System.out.println("Tentando abrir detalhes para ID: " + promissoriaId);
        Promissoria promissoria = promissoriaDAO.findById(promissoriaId);

        if (promissoria != null) {
            System.out.println("Promissória encontrada: " + promissoria);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/epromissorias/DetalhesPromissoriasView.fxml"));
                Scene scene = new Scene(loader.load(), 800, 600);

                DetalhesPromissoriasController detailsController = loader.getController();
                detailsController.setPromissoria(promissoria);

                Stage stage = (Stage) listView.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Detalhes da Promissória");
                stage.show();
            } catch (IOException e) {
                showAlert("Erro", "Não foi possível abrir a tela de detalhes: " + e.getMessage());
            }
        } else {
            showAlert("Erro", "Promissória não encontrada.");
        }
    }



}
