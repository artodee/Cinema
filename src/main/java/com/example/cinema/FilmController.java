package com.example.cinema;

import Models.Client;
import Models.Film;
import bdd.ClientManager;
import bdd.FilmManager;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.scene.control.Alert;


public class FilmController {


    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private DatePicker date;

    @FXML
    private JFXListView<Film> filmList;

    @FXML
    private ChoiceBox<String> genreList;

    @FXML
    private TextField nom;

    @FXML
    private AnchorPane slider;

    private Stage stage;
    private Scene scene;
    private Parent root;

    ObservableList<Film> items2 = FXCollections.observableArrayList();
    ObservableList<String> genreItems = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        //lier items à la listeView au démarrage
        filmList.setItems(items2);
        genreList.setItems(genreItems);


        loadFilm();
        loadGenre();

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

    @FXML
    public void add_film(ActionEvent event) throws SQLException {
        LocalDate date = this.date.getValue();
        String genre = this.genreList.getValue();
        String nom = this.nom.getText();


        if (date == null || genre.isEmpty() || nom.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs : date, genre et nom.");
            alert.showAndWait();
        } else {
            try {
                // Vérification du format date français (dd-MM-yyyy)
                Date sqlDate = Date.valueOf(date);

                // Si la date est valide, ajout du film
                FilmManager cm = new FilmManager();
                cm.addFilm(nom, genre, sqlDate.toString());
                this.loadFilm();
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez entrer une date valide au format jj-MM-aaaa (ex: 21-01-2025).");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void del_film(ActionEvent event) {
        Film selectedItem = filmList.getSelectionModel().getSelectedItem();
        if (selectedItem!= null) {
            FilmManager cm = new FilmManager();
            cm.delFilm(selectedItem.getId());
            this.loadFilm();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez selectionner un film à supprimer.");
            alert.showAndWait();
        }
    }



    private void loadFilm(){
        this.items2.clear();

        try {
            FilmManager fm = new FilmManager();
            ResultSet rs = fm.getFilms();


            while (rs.next()) {
                String date = rs.getString("date");
                String genre = rs.getString("genre");
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Film film = new Film(nom, genre, date, id);
                this.items2.add(film);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadGenre() {
        genreItems.clear();
        genreItems.addAll("Comédie", "Drame", "Comédie dramatique", "Thriller", "Action/Aventure", "Horreur", "Science-fiction", "Fantastique", "Animation", "Musical", "Documentaire", "Guerre", "Western", "Comédie romantique" ); // Numéros de séance prédéfinis
        genreList.setItems(genreItems);
    }

}
