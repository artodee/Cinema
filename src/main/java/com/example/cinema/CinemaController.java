package com.example.cinema;

import bdd.ClientManager;
import bdd.FilmManager;
import bdd.SeanceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

public class CinemaController {

    private ClientManager clientManager;
    private FilmManager filmManager;
    private SeanceManager seanceManager;

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Label lblNombreClients;

    @FXML
    private Label lblNombreFilms;

    @FXML
    private Label lblNombreSeances;

    public void initialize() {
        // Initialiser les objets AVANT de les utiliser
        this.clientManager = new ClientManager();
        this.filmManager = new FilmManager();
        this.seanceManager = new SeanceManager();

        updateStats();
    }

    public void updateStats() {

        int nombreClients = clientManager.getNombreClients();
        int nombreFilms = filmManager.getNombreFilms();
        int nombreSeances = seanceManager.getNombreSeances();

        lblNombreClients.setText("Nombre de clients inscrits : " + nombreClients);
        lblNombreFilms.setText("Nombre de films enregistrés : " + nombreFilms);
        lblNombreSeances.setText("Nombre de séances programmées : " + nombreSeances);
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
    private void versUtilisateur(ActionEvent event) throws IOException {
        switchScene(event, "Utilisateurs.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
