package com.demo.epromissorias.dao;

import com.demo.epromissorias.connection.DatabaseConnection;
import com.demo.epromissorias.entities.Adiantamento;
import com.demo.epromissorias.entities.Compra;
import com.demo.epromissorias.entities.Promissoria;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.demo.epromissorias.connection.DatabaseConnection.getConnection;

public class PromissoriaDAO {
    public void savePromissoria(Promissoria promissoria) {
        String sql = "INSERT INTO promissorias (nome, endereco, cpf, telefone, data_compra, data_pagamento, valor_total) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, promissoria.getNome());
            pstmt.setString(2, promissoria.getEndereco());
            pstmt.setString(3, promissoria.getCpf());
            pstmt.setString(4, promissoria.getTelefone());
            pstmt.setDate(5, java.sql.Date.valueOf(promissoria.getDataCompra())); // Use valueOf para LocalDate
            pstmt.setDate(6, java.sql.Date.valueOf(promissoria.getDataPagamento())); // Use valueOf para LocalDate
            pstmt.setDouble(7, promissoria.getValorTotal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Promissoria> findPromissoriaByName(String nome) {
        List<Promissoria> promissorias = new ArrayList<>();
        String sql = "SELECT * FROM promissorias WHERE nome LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nome + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Promissoria promissoria = new Promissoria();
                promissoria.setId(rs.getInt("id"));
                promissoria.setNome(rs.getString("nome"));
                promissoria.setEndereco(rs.getString("endereco"));
                promissoria.setCpf(rs.getString("cpf"));
                promissoria.setTelefone(rs.getString("telefone"));
                promissoria.setDataCompra(rs.getDate("data_compra").toLocalDate());
                promissoria.setDataPagamento(rs.getDate("data_pagamento").toLocalDate());
                promissoria.setValorTotal(rs.getDouble("valor_total"));
                promissorias.add(promissoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promissorias;
    }

    public List<Promissoria> findAll() {
        List<Promissoria> promissorias = new ArrayList<>();
        String sql = "SELECT * FROM promissorias"; // Query to fetch only the name and purchase date

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { // Execute the query

            while (rs.next()) {
                Promissoria promissoria = new Promissoria();
                promissoria.setNome(rs.getString("nome")); // Set the name
                promissoria.setDataCompra(rs.getDate("data_compra").toLocalDate()); // Set the purchase date
                // Optional: If you want to keep the ID, you could set a default value or remove this line.
                promissoria.setId(rs.getInt("id")); // Only if you want to keep the ID, but it's not needed now.
                promissorias.add(promissoria); // Add to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promissorias; // Return the list of promissórias
    }
    public Promissoria findById(int id) {
        Promissoria promissoria = null; // Inicializa a variável como nula
        String sql = "SELECT * FROM promissorias WHERE id = ?"; // Consulta para buscar pela ID

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id); // Define o ID na consulta
            ResultSet rs = pstmt.executeQuery(); // Executa a consulta

            if (rs.next()) { // Verifica se há resultados
                promissoria = new Promissoria();
                promissoria.setId(rs.getInt("id")); // Define o ID
                promissoria.setNome(rs.getString("nome")); // Define o nome
                promissoria.setEndereco(rs.getString("endereco")); // Define o endereço
                promissoria.setCpf(rs.getString("cpf")); // Define o CPF
                promissoria.setTelefone(rs.getString("telefone")); // Define o telefone
                promissoria.setDataCompra(rs.getDate("data_compra").toLocalDate()); // Define a data da compra
                promissoria.setDataPagamento(rs.getDate("data_pagamento") != null ? rs.getDate("data_pagamento").toLocalDate() : null); // Define a data de pagamento (pode ser nula)
                promissoria.setValorTotal(rs.getDouble("valor_total")); // Define o valor total

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trata exceções
        }
        return promissoria; // Retorna a promissória encontrada ou nula se não encontrada
    }

    public void deletePromissoria(int id) {
        // Verifique se há compras associadas
        String query = "SELECT COUNT(*) FROM compras WHERE promissoria_id = ?";
        try (Connection conn = getConnection(); // Use getConnection() to obtain a connection
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new IllegalStateException("Não é possível excluir a promissória, pois existem compras associadas.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Tratamento de exceções
        }

        // Se não houver compras, prossiga com a exclusão
        String deleteQuery = "DELETE FROM promissorias WHERE id = ?";
        try (Connection conn = getConnection(); // Use getConnection() here as well
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Tratamento de exceções
        }
    }



    public void updatePromissoria(Promissoria promissoria) {
        String sql = "UPDATE promissorias SET nome = ?, endereco = ?, cpf = ?, telefone = ?, data_compra = ?, data_pagamento = ?, valor_total = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, promissoria.getNome());
            pstmt.setString(2, promissoria.getEndereco());
            pstmt.setString(3, promissoria.getCpf());
            pstmt.setString(4, promissoria.getTelefone());
            pstmt.setDate(5, java.sql.Date.valueOf(promissoria.getDataCompra())); // Use valueOf para LocalDate
            pstmt.setDate(6, java.sql.Date.valueOf(promissoria.getDataPagamento())); // Use valueOf para LocalDate
            pstmt.setDouble(7, promissoria.getValorTotal());
            pstmt.setInt(8, promissoria.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Compra> getComprasByPromissoriaId(int promissoriaId) {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT id, valor, data_compra FROM compras WHERE promissoria_id = ?"; // Certifique-se de que está usando o nome correto da coluna

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, promissoriaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id"); // Recuperando o ID da compra
                double valor = rs.getDouble("valor");
                LocalDate data = rs.getDate("data_compra").toLocalDate(); // Use "data_compra" se for o nome correto da coluna
                compras.add(new Compra(id, valor, data)); // Adicionando a compra com ID, valor e data
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compras;
    }
    public List<Adiantamento> getAdiantamentosByPromissoriaId(int promissoriaId) {
        List<Adiantamento> adiantamentos = new ArrayList<>();
        String sql = "SELECT id, valor, data FROM adiantamento WHERE promissoria_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promissoriaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double valor = rs.getDouble("valor");
                LocalDate data = rs.getDate("data").toLocalDate(); // Use "data_compra" se for o nome correto da coluna
                adiantamentos.add(new Adiantamento(id, valor, data)); // Adicionando a compra com ID, valor e data
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adiantamentos;
    }
    public void addAdiantamento(Adiantamento adiantamento, int promissoriaId) throws SQLException {
        String sql = "INSERT INTO adiantamento (valor, data, promissoria_id) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, adiantamento.getValor());
            stmt.setDate(2, Date.valueOf(adiantamento.getData()));
            stmt.setInt(3, promissoriaId);
            stmt.executeUpdate();
        }
    }
    public Promissoria findByCpf(String cpf) {
        String sql = "SELECT * FROM promissorias WHERE cpf = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf); // Define o CPF no parâmetro da consulta
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Verifica se encontrou um resultado
                Promissoria promissoria = new Promissoria();
                promissoria.setId(resultSet.getInt("id")); // Supondo que você tenha um campo ID
                promissoria.setNome(resultSet.getString("nome"));
                promissoria.setEndereco(resultSet.getString("endereco"));
                promissoria.setCpf(resultSet.getString("cpf"));
                promissoria.setTelefone(resultSet.getString("telefone"));
                promissoria.setDataCompra(resultSet.getDate("data_compra").toLocalDate());
                promissoria.setDataPagamento(resultSet.getDate("data_pagamento").toLocalDate());
                return promissoria; // Retorna a promissória encontrada
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trata a exceção como necessário
        }

        return null; // Retorna null se não encontrar nenhuma promissória com o CPF fornecido
    }





    public boolean compraExists(int compraId) {
        String sql = "SELECT COUNT(*) FROM compras WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compraId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se a compra existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false se ocorrer um erro ou não existir
    }

    public void deleteCompra(int compraId) {
        if (!compraExists(compraId)) {
            System.out.println("Não é possível deletar. Compra com ID " + compraId + " não encontrada.");
            return; // Sai do método se a compra não existir
        }

        String sql = "DELETE FROM compras WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compraId);
            pstmt.executeUpdate();
            System.out.println("Compra deletada com sucesso. ID: " + compraId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteAllComprasByPromissoria(int promissoriaId) {
        String sql = "DELETE FROM compras WHERE promissoria_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promissoriaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteAllAdiantamentosByPromissoria(int promissoriaId) {
        String sql = "DELETE FROM adiantamento WHERE promissoria_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promissoriaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addCompraToPromissoria(int promissoriaId, Compra compra) {
        String sql = "INSERT INTO compras (promissoria_id, valor, data_compra) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, promissoriaId);
            pstmt.setDouble(2, compra.getValor());
            pstmt.setDate(3, java.sql.Date.valueOf(compra.getData())); // Use valueOf para LocalDate
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
