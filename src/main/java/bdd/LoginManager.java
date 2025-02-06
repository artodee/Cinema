package bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginManager {

    public boolean validateLogin(String firstname, String lastname, String password) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sqlRequest = "SELECT * FROM login WHERE firstname = ? AND lastname = ? AND password = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true; // L'utilisateur existe et les identifiants sont corrects
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mauvais identifiants
    }
}