package com.example.cinema;

import Models.Client;
import Models.Film;
import Models.Seance;
import bdd.ClientManager;
import com.jfoenix.controls.JFXCheckBox;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CinemaController {

    public ImageView exit;
    @FXML
    private JFXCheckBox checkbox12;
    
    @FXML
    private TextField nomC;

    @FXML
    private TextField prenom;

    @FXML
    private TextField mail;
    @FXML
    private ListView<Client> clientList;

    @FXML
    private JFXListView<Film> filmList;

    @FXML
    private JFXListView<Seance> seanceList;

    private String title;
    private String message;



    ObservableList<Client> items = FXCollections.observableArrayList();
    ObservableList<Film> items2 = FXCollections.observableArrayList();
    ObservableList<Seance> items3 = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Lier items à la listeView au démarrage
        this.clientList.setItems(items);
        filmList.setItems(items2);
        seanceList.setItems(items3);

        loadClients();
    }

    // Méthode de navigation vers la page Film
    public void versFilm(ActionEvent actionEvent) {
        navigateToPage("Films.fxml", actionEvent);
    }


    // Méthode de navigation vers la page Seance
    public void versSeance(ActionEvent actionEvent) {
        navigateToPage("Seance.fxml", actionEvent);
    }

    // Méthode de navigation vers la page Client
    public void versClient(ActionEvent actionEvent) {
        navigateToPage("Clients.fxml", actionEvent);
    }

    // Méthode de navigation générique
    private void navigateToPage(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent page = loader.load();
            Scene scene = new Scene(page);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Chargement des clients dans la ListView
    private void loadClients() {
        items.clear();
        try {
            ClientManager cm = new ClientManager();
            ResultSet rs = cm.getClients();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String mail = rs.getString("mail");

                Client client = new Client(id, nom, prenom, mail, clientList);
                items.add(client); // Ajout à la liste observable
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les clients : " + e.getMessage());
        }
    }

    public void add_client(ActionEvent actionEvent) {
        String nom = nomC.getText().trim();
        String prenom = this.prenom.getText().trim();
        String email = this.mail.getText().trim();

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!email.matches("^(.+)@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Adresse email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

    }

}