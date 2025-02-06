package bdd;

import java.sql.*;

public class SeanceManager {

    public boolean seanceExist(int numSeance, int filmId) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sqlRequest = "SELECT COUNT(*) FROM seances WHERE num_seance = ? AND films_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setInt(1, numSeance);
            pstmt.setInt(2, filmId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Séance existante
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Pas de séance existante
    }

    public void addSeance(int numSeanceField, int clientId, int filmId, Double Prix) {
        if (seanceExist(numSeanceField, filmId)) {
            throw new RuntimeException("Une séance avec ce film existe déjà pour ce numéro de séance.");
        }
        BddManager bddManager = new BddManager();
        Connection connection = bddManager.connection();
        String sqlRequest = "INSERT INTO seances (num_seance, films_id, clients_id, prix) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setInt(1, numSeanceField);
            pstmt.setInt(2, filmId);
            pstmt.setInt(3, clientId);
            pstmt.setDouble(4, Prix);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la séance", e);
        }
    }

    public void delSeance(int id) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "DELETE FROM seances WHERE id =?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la séance : " + e.getMessage(), e);
        }
    }

    public ResultSet getSeance() {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "SELECT * FROM seances";

        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql_request);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des séances : " + e.getMessage(), e);
        }
    }

    public int getNombreSeances() {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        ResultSet rs = null;
        int count = 0;
        String query = "SELECT COUNT(*) FROM seances";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
