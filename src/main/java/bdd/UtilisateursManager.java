package bdd;

import java.sql.*;

public class UtilisateursManager {

    public boolean UtilisateurExist(String firstname, String lastname, String password) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sqlRequest = "SELECT COUNT(*) FROM login WHERE firstname = ? AND lastname = ? AND password = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Utilisateur existants
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Pas d'utilsateur existants
    }

    public void addUtilisateur(String firstname, String lastname, String password) {
        if (UtilisateurExist(firstname, lastname,password)) {
            throw new RuntimeException("Un utilisateur existe deja");
        }
        BddManager bddManager = new BddManager();
        Connection connection = bddManager.connection();
        String sqlRequest = "INSERT INTO login (firstname, lastname, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du client", e);
        }
    }

    public void delUtilisateur(int id) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "DELETE FROM login WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setInt(1, id);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public ResultSet getUtilisateur() {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "SELECT * FROM login";

        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql_request);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du clients : " + e.getMessage(), e);
        }
    }
}
