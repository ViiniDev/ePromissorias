package com.demo.epromissorias.controllers;

import com.demo.epromissorias.entities.Adiantamento;
import com.demo.epromissorias.entities.Compra;
import com.demo.epromissorias.entities.Promissoria;
import com.demo.epromissorias.dao.PromissoriaDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class DetalhesPromissoriasController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField enderecoField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField telefoneField;
    @FXML
    private TextField dataCompraField;
    @FXML
    private TextField dataPagamentoField;
    @FXML
    private TextField valorTotalField;
    @FXML
    private TableView<Compra> comprasTable;
    @FXML
    private TableColumn<Compra, Double> valorCompraColumn;
    @FXML
    private TableColumn<Compra, String> dataCompraColumn;
    @FXML
    private Button adiantarButton;
    @FXML
    private TextField valorAPagarField;
    @FXML
    private TextField adiantamentoField;
    @FXML
    private TableView<Adiantamento> adiantamentosTable;
    @FXML
    private TableColumn<Adiantamento, Double> valorAdiantamentoColumn;
    @FXML
    private TableColumn<Adiantamento, LocalDate> dataAdiantamentoColumn;


    private PromissoriaDAO promissoriaDAO;
    private Promissoria promissoria;

    private ObservableList<Compra> comprasList = FXCollections.observableArrayList();
    private ObservableList<Adiantamento> adiantamentosList = FXCollections.observableArrayList();
    public DetalhesPromissoriasController() {
        this.promissoriaDAO = new PromissoriaDAO();
    }

    @FXML
    public void initialize() {
        valorCompraColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValor()).asObject());
        dataCompraColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData().toString()));
        atualizarValorTotal();
        valorAdiantamentoColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValor()).asObject());
        dataAdiantamentoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getData()));

        // Adicionando listener para a seleção de compras
        comprasTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Compra>() {
            @Override
            public void changed(ObservableValue<? extends Compra> observable, Compra oldValue, Compra newValue) {
                if (newValue != null) {
                    System.out.println("Compra selecionada: ID = " + newValue.getId()); // Loga o ID da compra selecionada
                }
            }
        });

    }
    @FXML
    private void adiantarValor() {
        // Atualiza o valor total antes da verificação
        atualizarValorTotal();

        String valorStr = adiantamentoField.getText();

        // Verifica se o campo de adiantamento está vazio
        if (valorStr.isEmpty()) {
            showAlert("Erro", "Por favor, insira um valor de adiantamento.");
            return;
        }

        double valorAdiantamento;
        try {
            valorAdiantamento = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            showAlert("Erro", "Por favor, insira um valor numérico válido para o adiantamento.");
            return;
        }

        // Verifica se o valor total da promissória é maior que 0
        double valorTotalAtual = Double.parseDouble(valorTotalField.getText().replace(",", "."));
        if (valorTotalAtual <= 0) {
            showAlert("Erro", "Não é possível adicionar adiantamento. O valor total da promissória já foi quitado ou é inválido.");
            return;
        }

        // Verifica se o valor do adiantamento é maior que o valor total
        if (valorAdiantamento > valorTotalAtual) {
            showAlert("Erro", "O valor do adiantamento não pode ser maior que o valor total da promissória.");
            return;
        }

        // Se o valor do adiantamento for igual ao valor total, quita a dívida
        if (valorAdiantamento == valorTotalAtual) {
            showAlert("Sucesso", "Adiantamento adicionado! A dívida foi quitada.");
            // Zera o valor a pagar da promissória ao quitar a dívida
            // Desabilita o botão de adicionar adiantamento
            adiantamentoField.setDisable(true); // Desabilite o campo de adiantamento
            adiantarButton.setDisable(true);
        } else {
            // Reduz o valor total da promissória pelo valor do adiantamento
            promissoria.setValorTotal(valorTotalAtual - valorAdiantamento);
            showAlert("Sucesso", "Adiantamento adicionado com sucesso!");
            adiantamentoField.clear();
        }

        Adiantamento adiantamento = new Adiantamento(0, valorAdiantamento, LocalDate.now());
        try {
            promissoriaDAO.addAdiantamento(adiantamento, promissoria.getId());
            atualizarTabelaAdiantamentos(); // Atualiza a tabela de adiantamentos

            // Recalcula o valor a pagar após adicionar o adiantamento
            atualizarValorAPagar(); // Atualiza o valor a pagar

            // Verifica se o valor a pagar ficou igual a zero
            double valorAPagarAtual = Double.parseDouble(valorAPagarField.getText().replace(",", "."));
            if (valorAPagarAtual == 0) { // Mudança aqui para verificar valor a pagar
                showAlert("Sucesso", "A conta foi quitada! Não é possível adicionar mais adiantamentos.");
                adiantamentoField.setDisable(true); // Desabilita o campo de adiantamento
                handleDeleteAllCompras();

            }

        } catch (SQLException e) {
            showAlert("Erro", "Erro ao adicionar o adiantamento: " + e.getMessage());
        }
    }



    private void atualizarTabelaAdiantamentos() throws SQLException {
        List<Adiantamento> adiantamentos = promissoriaDAO.getAdiantamentosByPromissoriaId(promissoria.getId());
        adiantamentosList.clear();
        adiantamentosList.addAll(adiantamentos);
        adiantamentosTable.setItems(adiantamentosList);
        atualizarValorAPagar();
    }

    private double atualizarValorAPagar() throws SQLException {

        // Soma o valor total das compras
        double totalCompras = comprasList.stream()
                .mapToDouble(Compra::getValor)
                .sum();

        // Calcula o total de adiantamentos da promissória
        double totalAdiantamentos = adiantamentosList.stream()
                .mapToDouble((Adiantamento::getValor))
                .sum();

        // Calcula o valor a pagar
        double valorAPagar = totalCompras - totalAdiantamentos;

        // Aqui você deve ter um campo de texto para mostrar o valor a pagar
        valorAPagarField.setText(String.format("%.2f", valorAPagar));
    return valorAPagar;
    }



    private void listarCompras(int promissoriaId) {
        List<Compra> compras = promissoriaDAO.getComprasByPromissoriaId(promissoriaId);
        comprasList.clear();
        comprasList.addAll(compras);
        comprasTable.setItems(comprasList);
        atualizarValorTotal();
    }
    private void atualizarValorTotal() {

        double valorTotal = comprasList.stream()
                .mapToDouble(Compra::getValor)
                .sum(); // Soma todos os valores das compras
        valorTotalField.setText(String.format("%.2f", valorTotal)); // Formata para duas casas decimais
    }

    public void setPromissoria(Promissoria promissoria) {
        this.promissoria = promissoria;
        nomeField.setText(promissoria.getNome());
        enderecoField.setText(promissoria.getEndereco());
        cpfField.setText(promissoria.getCpf());
        telefoneField.setText(promissoria.getTelefone());
        dataCompraField.setText(promissoria.getDataCompra().toString());
        dataPagamentoField.setText(promissoria.getDataPagamento() != null ? promissoria.getDataPagamento().toString() : "N/A");
        valorTotalField.setText(String.valueOf(promissoria.getValorTotal()));
        listarCompras(promissoria.getId());
        try {
            atualizarTabelaAdiantamentos(); // Adicione esta linha
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao atualizar a tabela de adiantamentos: " + e.getMessage());
        }
    }

    @FXML
    private void alterarPromissoria() {
        if (promissoria != null) {
            // Captura os dados dos campos
            String nome = nomeField.getText();
            String endereco = enderecoField.getText();
            String cpf = cpfField.getText();
            String telefone = telefoneField.getText();
            double valorTotal;

            // Validação do CPF
            if (!isCpfValido(cpf)) {
                showAlert("CPF inválido", "O CPF deve conter 11 dígitos numéricos.");
                return;
            }

            // Validação do telefone
            if (!isTelefoneValido(telefone)) {
                showAlert("Telefone inválido", "O telefone deve estar no formato: XX XXXXX-XXXX ou XX XXXXXXXX.");
                return;
            }


            // Atualiza os dados da promissória
            promissoria.setNome(nome);
            promissoria.setEndereco(endereco);
            promissoria.setCpf(cpf);
            promissoria.setTelefone(telefone);

            try {
                promissoriaDAO.updatePromissoria(promissoria);
                showAlert("Sucesso", "Promissória alterada com sucesso!");
            } catch (Exception e) {
                showAlert("Erro", "Erro ao tentar atualizar a promissória no banco de dados.");
            }
        } else {
            showAlert("Erro", "Nenhuma promissória encontrada para alterar.");
        }
    }

    @FXML
    private void excluirPromissoria() {
        if (promissoria != null) {
            promissoriaDAO.deletePromissoria(promissoria.getId());
            showAlert("Sucesso", "Promissória excluída com sucesso!");
            voltarParaListagemPromissorias();
        } else {
            showAlert("Erro", "Promissória não encontrada.");
        }
    }

    @FXML
    private void deletarCompra() {
        Compra compraSelecionada = comprasTable.getSelectionModel().getSelectedItem();
        if (compraSelecionada != null) {
            try {
                // Remove a compra da lista exibida na interface
                comprasList.remove(compraSelecionada);

                // Remove a compra do banco de dados
                promissoriaDAO.deleteCompra(compraSelecionada.getId());

                // Alerta de sucesso
                showAlert("Sucesso", "Compra deletada com sucesso!");
                atualizarValorTotal();
                atualizarValorAPagar();
            } catch (Exception e) {
                // Alerta de erro
                showAlert("Erro", "Erro inesperado ao deletar a compra.");
                e.printStackTrace(); // Consider logging this
            }
        } else {
            // Alerta se nenhuma compra for selecionada
            showAlert("Erro", "Selecione uma compra para deletar.");
        }
    }

    @FXML
    private void handleDeleteAllCompras() {
        if (promissoria != null) {
            try {
                promissoriaDAO.deleteAllComprasByPromissoria(promissoria.getId());
                showAlert("Sucesso", "Todas as compras foram deletadas com sucesso.");
                listarCompras(promissoria.getId());
            } catch (Exception e) {
                showAlert("Erro", "Erro ao deletar compras: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Erro", "Nenhuma promissória selecionada.");
        }
    }
    @FXML
    private void handleDeleteAllAdiantamentos(){
        if (promissoria != null) {
            try {
                promissoriaDAO.deleteAllAdiantamentosByPromissoria(promissoria.getId());
                showAlert("Sucesso", "Todos adiantamentos foram deletados com sucesso. ");
                atualizarTabelaAdiantamentos();
                atualizarValorAPagar();
            } catch (Exception e) {
                showAlert("Erro", "Erro ao deletar adiantamentos: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
                showAlert("Erro", "Nenhuma adiantamento.");
            }
        }

    @FXML
    public void adicionarCompra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/epromissorias/AddCompraPromissoriaView.fxml"));
            Parent root = loader.load();

            AddCompraController controller = loader.getController();
            controller.setPromissoriaId(promissoria.getId());

            Stage stage = new Stage();
            stage.setTitle("Adicionar Compra");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            listarCompras(promissoria.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void voltarParaListagemPromissorias() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/epromissorias/ListagemPromissoriasView.fxml"));
            Parent listagemPromissoriasRoot = loader.load();
            nomeField.getScene().setRoot(listagemPromissoriasRoot);
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível carregar a tela de listagem de promissórias.");
        }
    }
    private boolean isCpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}"); // CPF com 11 dígitos
    }

    // Validação do telefone
    private boolean isTelefoneValido(String telefone) {
        String telefoneRegex = "^\\d{2}\\s\\d{8,9}$";
        return Pattern.matches(telefoneRegex, telefone);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
