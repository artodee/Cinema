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


}
