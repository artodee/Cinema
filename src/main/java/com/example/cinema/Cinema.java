package com.example.cinema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Cinema extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Charger le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Accueil.fxml"));

            // Charger l'interface et obtenir le contrôleur
            Scene scene = new Scene(fxmlLoader.load(), 700, 400); // Taille de la fenêtre
            scene.getStylesheets().add(CinemaController.class.getResource("styles.css").toExternalForm());

            // Obtenir le contrôleur après le chargement
            CinemaController controller = fxmlLoader.getController();

            // Configurer le stage
            stage.setTitle("Gestion de Cinéma");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}