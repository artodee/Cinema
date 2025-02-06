package com.example.cinema;

import bdd.LoginManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton login;

    @FXML
    private PasswordField password;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private void CancelOnAction(ActionEvent event) {
        // Fermer la fenêtre de connexion
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void LoginOnAction(ActionEvent event) throws IOException {
        String enteredFirstname = firstname.getText();
        String enteredLastname = lastname.getText();
        String enteredPassword = password.getText();

        if (enteredFirstname.isEmpty() || enteredLastname.isEmpty() || enteredPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avec des informations valides.");
            alert.showAndWait();
        } else {
            LoginManager loginManager = new LoginManager();
            boolean isValid = loginManager.validateLogin(enteredFirstname, enteredLastname, enteredPassword);

            if (isValid) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Connexion réussie");
                alert.setHeaderText(null);
                alert.setContentText("Bienvenue !");
                alert.showAndWait();

                // rediriger l'utilisateur vers l'accueil par exemple
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
                    Parent root = loader.load();
                    CinemaController cinemaController = loader.getController();
                    cinemaController.updateStats();// Charger le fichier FXML


                    // Changer la scène
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de connexion");
                alert.setHeaderText(null);
                alert.setContentText("Identifiants incorrects.");
                alert.showAndWait();

            }

            }
        }
    }