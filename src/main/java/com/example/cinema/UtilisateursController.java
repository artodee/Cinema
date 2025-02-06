package com.example.cinema;

import Models.Utilisateur;
import bdd.UtilisateursManager;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateursController {

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private TextField NomUtilisateur;

    @FXML
    private PasswordField PasswordUtilisateur;

    @FXML
    private TextField PrenomUtilisateur;

    @FXML
    private JFXListView<Utilisateur> UtilisateursList;

    private Stage stage;
    private Scene scene;
    private Parent root;

    ObservableList<Utilisateur> Utilisateuritems = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        //lier items à la listeView au démarrage
        UtilisateursList.setItems(Utilisateuritems);


        loadUtilisateur();

    }

    @FXML
    private void versAccueil(ActionEvent event) throws IOException {
        switchScene(event, "Accueil.fxml");
    }

    @FXML
    private void versFilm(ActionEvent event) throws IOException {
        switchScene(event, "Film.fxml");
    }

    @FXML
    private void versSeance(ActionEvent event) throws IOException {
        switchScene(event, "Seance.fxml");
    }

    @FXML
    private void versClient(ActionEvent event) throws IOException {
        switchScene(event, "Client.fxml");
    }

    @FXML
    void versUtilisateur(ActionEvent event) throws IOException {
        switchScene(event, "Utilisateurs.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void add_utilisateur(ActionEvent event) {
        String prenomUtilisateur = this.PrenomUtilisateur.getText();
        String nomUtilisateur = this.NomUtilisateur.getText();
        String password = this.PasswordUtilisateur.getText();


        if (prenomUtilisateur.isEmpty() || nomUtilisateur.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avec des informations valides");
            alert.showAndWait();
        } else if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
            alert.showAndWait();
        } else {
            UtilisateursManager um = new UtilisateursManager();
            um.addUtilisateur(prenomUtilisateur, nomUtilisateur, password);
            this.loadUtilisateur();
        }

    }

    @FXML
    private void del_utilisateur(ActionEvent event) {
        Utilisateur selectedUser = UtilisateursList.getSelectionModel().getSelectedItem();

        if (selectedUser!= null) {
            UtilisateursManager um = new UtilisateursManager();
            um.delUtilisateur(selectedUser.getId());
            this.loadUtilisateur();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez selectionner un utilisateur à supprimer");
            alert.showAndWait();
        }
    }

    private void loadUtilisateur(){
        this.Utilisateuritems.clear();

        try {
            UtilisateursManager um = new UtilisateursManager();
            ResultSet rs = um.getUtilisateur();


            while (rs.next()) {
                String nomUtilisateur = rs.getString("lastname");
                String prenomUtilisateur = rs.getString("firstname");
                int id = rs.getInt("id");
                String password = rs.getString("password");
                Utilisateur utilisateur = new Utilisateur(id, nomUtilisateur, prenomUtilisateur, password);
                this.Utilisateuritems.add(utilisateur);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
