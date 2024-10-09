package com.demo.epromissorias.controllers;

import com.demo.epromissorias.dao.PromissoriaDAO;
import com.demo.epromissorias.entities.Promissoria;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class CadastroPromissoriaController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField enderecoField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField telefoneField;
    @FXML
    private DatePicker dataCompraPicker;
    @FXML
    private DatePicker dataPagamentoPicker;

    private PromissoriaDAO promissoriaDAO = new PromissoriaDAO();

    @FXML
    private void handleCadastrarPromissoria() {
        // Captura os dados dos campos
        String nome = nomeField.getText();
        String endereco = enderecoField.getText();
        String cpf = cpfField.getText();
        String telefone = telefoneField.getText();
        LocalDate dataCompra = dataCompraPicker.getValue();
        LocalDate dataPagamento = dataPagamentoPicker.getValue();

        // Validação do CPF
        if (!isCpfValido(cpf)) {
            mostrarMensagemErro("CPF inválido", "O CPF deve conter 11 dígitos numéricos.");
            return;
        }

        // Verifica se o CPF já está cadastrado
        if (promissoriaDAO.findByCpf(cpf) != null) {
            mostrarMensagemErro("CPF já cadastrado", "Este CPF já está cadastrado no sistema.");
            return;
        }

        // Validação do telefone
        if (!isTelefoneValido(telefone)) {
            mostrarMensagemErro("Telefone inválido", "O telefone deve estar no formato: XX XXXXX-XXXX ou XX XXXXXXXX.");
            return;
        }

        // Cria um objeto Promissoria
        Promissoria promissoria = new Promissoria();
        promissoria.setNome(nome);
        promissoria.setEndereco(endereco);
        promissoria.setCpf(cpf);
        promissoria.setTelefone(telefone);
        promissoria.setDataCompra(dataCompra);
        promissoria.setDataPagamento(dataPagamento);

        // Salva a promissória no banco de dados
        promissoriaDAO.savePromissoria(promissoria);

        // Exibe mensagem de sucesso
        mostrarMensagemSucesso("Cadastro realizado com sucesso!");

        // Limpa os campos após o cadastro
        clearFields();
    }

    // Validação simples de CPF
    private boolean isCpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}"); // CPF com 11 dígitos
    }

    // Validação do telefone
    private boolean isTelefoneValido(String telefone) {
        String telefoneRegex = "^\\d{2}\\s\\d{8,9}$";
        return Pattern.matches(telefoneRegex, telefone);
    }

    // Exibe uma mensagem de erro
    private void mostrarMensagemErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Exibe uma mensagem de sucesso
    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Limpa os campos
    private void clearFields() {
        nomeField.clear();
        enderecoField.clear();
        cpfField.clear();
        telefoneField.clear();
        dataCompraPicker.setValue(null);
        dataPagamentoPicker.setValue(null);
    }
}
