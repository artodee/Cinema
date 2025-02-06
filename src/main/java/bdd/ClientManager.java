package bdd;

import java.sql.*;

public class ClientManager {

    public boolean addClient(String nom, String prenom, String mail){
        BddManager bddManager = new BddManager();
        Connection Connection = bddManager.connection();
        String sql_request = "INSERT INTO clients (nom, prenom, mail) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = Connection.prepareStatement(sql_request);
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, mail);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void delClient(int id){
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "DELETE FROM clients WHERE id =?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setInt(1, id);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getClients(){
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        ResultSet rs = null;
        String sql_request = "SELECT * FROM clients";
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sql_request);
            //System.out.println(rs);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNombreClients() {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        int count = 0;
        String query = "SELECT COUNT(*) FROM clients";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de clients : " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
        return count;
    }


}
