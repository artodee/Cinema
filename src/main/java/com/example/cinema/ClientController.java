package com.example.cinema;

import Models.Client;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import bdd.ClientManager;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientController {


    //service
    @FXML
    private ListView<Client> ClientList;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;



    @FXML
    private TextField mail;

    @FXML
    private TextField nomC;

    @FXML
    private TextField prenom;

    private Stage stage;
    private Scene scene;
    private Parent root;


    ObservableList<Client> items = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        //lier items à la listeView au démarrage
        ClientList.setItems(items);


        loadClients();

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
    public void add_client(ActionEvent event) throws SQLException {
        String prenom = this.prenom.getText();
        String nomC = this.nomC.getText();
        String mail = this.mail.getText();


        if (prenom.isEmpty() || nomC.isEmpty() || mail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avec des informations valides");
            alert.showAndWait();
        } else if (!mail.matches("^(.+)@(.+)$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une adresse email valide");
            alert.showAndWait();
        } else {
            ClientManager cm = new ClientManager();
            cm.addClient(prenom, nomC, mail);
            this.loadClients();
        }
    }

    @FXML
    public void del_client(ActionEvent event) {
        Client selectedClient = ClientList.getSelectionModel().getSelectedItem();

        if (selectedClient!= null) {
            ClientManager cm = new ClientManager();
            cm.delClient(selectedClient.getId());
            this.loadClients();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Aucun client sélectionné");
            alert.showAndWait();
        }
    }


    private void loadClients(){
        this.items.clear();

        try {
            ClientManager cm = new ClientManager();
            ResultSet rs = cm.getClients();


            while (rs.next()) {
                String prenom = rs.getString("prenom");
                String nomC = rs.getString("nom");
                int id = rs.getInt("id");
                String mail = rs.getString("mail");
                Client client = new Client(id, prenom, nomC, mail);
                this.items.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}