package com.example.cinema;

import Models.Client;
import Models.Film;
import Models.Seance;
import bdd.ClientManager;
import bdd.FilmManager;
import bdd.SeanceManager;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class SeanceController {

    @FXML
    private ChoiceBox<Client> statusField;

    @FXML
    private JFXListView<Seance> seanceList;

    @FXML
    private ChoiceBox<Film> filmChoice;

    @FXML
    private ChoiceBox<Double> PrixChoice;

    @FXML
    private ChoiceBox<Integer> numSeanceField;

    @FXML
    private AnchorPane slider;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Seance> items3 = FXCollections.observableArrayList();
    private ObservableList<Film> filmItems = FXCollections.observableArrayList();
    private ObservableList<Client> clientItems = FXCollections.observableArrayList();
    private ObservableList<Double> prixItems = FXCollections.observableArrayList();
    private ObservableList<Integer> seanceItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        seanceList.setItems(items3);
        filmChoice.setItems(filmItems);
        statusField.setItems(clientItems);
        numSeanceField.setItems(seanceItems);
        loadFilms();
        loadClients();
        loadPrix();
        loadSeance();
        loadNumSeances();

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
    public void add_seance(ActionEvent event) {
        try {
            Integer numSeance = numSeanceField.getValue(); // Récupère le numéro sélectionné
            if (numSeance == null) {
                showAlert("Erreur", "Veuillez sélectionner un numéro de séance.");
                return;
            }
            Client selectedClient = statusField.getValue();
            if (selectedClient == null) {
                showAlert("Erreur", "Veuillez sélectionner un client.");
                return;
            }
            int status = selectedClient.getId();
            // Films
            Film selectedFilm = filmChoice.getValue();
            if (selectedFilm == null) {
                showAlert("Erreur", "Veuillez sélectionner un film.");
                return;
            }
            int filmId = selectedFilm.getId(); // Récupère l'ID du film sélectionné
            // Prix
            Double selectedPrix = PrixChoice.getValue();
            if (selectedPrix == null) {
                showAlert("Erreur", "Veuillez sélectionner un prix.");
                return;
            }

            SeanceManager sm = new SeanceManager();
            sm.addSeance(numSeance, selectedClient.getId(), selectedFilm.getId(), selectedPrix); // Ajout avec le film_id
            loadSeance();

            // 🔥 Récupérer l'instance actuelle de CinemaController et mettre à jour les stats
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
            Parent root = loader.load();
            CinemaController cinemaController = loader.getController();
            cinemaController.updateStats();  // ✅ Met à jour le nombre de séances

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
        } catch (IOException e) {
            showAlert("Erreur", "Problème de chargement de la page Accueil.");
        } catch (RuntimeException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    public void del_seance(ActionEvent event) {
        Seance selectedSeance = seanceList.getSelectionModel().getSelectedItem();
        if (selectedSeance == null) {
            showAlert("Erreur", "Veuillez sélectionner une séance.");
            return;
        }
        SeanceManager sm = new SeanceManager();
        sm.delSeance(selectedSeance.getId());
        loadSeance();
    }

    private void loadSeance() {
        items3.clear();
        try {
            SeanceManager sm = new SeanceManager();
            ResultSet rs = sm.getSeance();

            while (rs.next()) {
                int id = rs.getInt("id");
                int numSeance = rs.getInt("num_seance");
                int clientId = rs.getInt("clients_id");
                int filmId = rs.getInt("films_id");
                double prix = rs.getDouble("prix");

                Client clientAssocie = null;
                for (Client client : clientItems) {
                    if (client.getId() == clientId) {
                        clientAssocie = client;
                        break;
                    }
                }
                // Trouver le film correspondant à l'ID
                Film filmAssocie = null;
                for (Film film : filmItems) {
                    if (film.getId() == filmId) {
                        filmAssocie = film;
                        break;
                    }
                }

                if (filmAssocie != null && clientAssocie != null) {
                    items3.add(new Seance(id, numSeance, filmAssocie, clientAssocie, prix));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadNumSeances() {
        seanceItems.clear();
        seanceItems.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10); // Numéros de séance prédéfinis
        numSeanceField.setItems(seanceItems);
    }

    private void loadFilms() {
        filmItems.clear();
        try {
            FilmManager fm = new FilmManager();
            ResultSet rs = fm.getFilms();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (rs.next()) {
                int id = rs.getInt("id");
                Date dateDB = rs.getDate("date");
                String genre = rs.getString("genre");
                String nom = rs.getString("nom");

                String formattedDate = (dateDB != null) ? dateDB.toLocalDate().format(formatter) : "Date inconnue";

                Film film = new Film(nom, genre, formattedDate, id);
                filmItems.add(film);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadClients() {
        clientItems.clear();
        try {
            ClientManager cm = new ClientManager(); // Assure-toi d'avoir une classe qui gère les clients
            ResultSet rs = cm.getClients(); // Méthode pour récupérer tous les clients

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomC = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String mail = rs.getString("mail");

                Client client = new Client(id, nomC, prenom, mail); // Adapte en fonction de ta classe Client
                clientItems.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadPrix() {
        prixItems.clear();
        prixItems.addAll(9.0, 10.0, 12.0, 15.0); // Ajoute les prix souhaités
        PrixChoice.setItems(prixItems);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    @FXML
    private void genererTicket() {
        Seance selectedSeance = seanceList.getSelectionModel().getSelectedItem();

        if (selectedSeance == null) {
            showAlert("Erreur", "Veuillez sélectionner une séance pour générer un ticket.");
            return;
        }

        String filePath = "ticket_seance_" + selectedSeance.getId() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, new PageSize(300, 500)); // Format vertical compact

            PdfCanvas canvas = new PdfCanvas(pdf.addNewPage());
            canvas.setFillColor(ColorConstants.BLACK)  // Fond noir
                    .rectangle(0, 0, 300, 500)
                    .fill();

            // 🔥 Ajout du logo
            String logoPath = "logo_pathe.png"; // Assurez-vous que ce fichier existe
            File logoFile = new File(logoPath);
            if (logoFile.exists()) {
                Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoPath));
                logo.setWidth(80).setHeight(50);
                logo.setFixedPosition(110, 440); // Position en haut
                document.add(logo);
            }

            // 🏆 Nom du film en grand et centré
            document.add(new Paragraph(selectedSeance.getFilm().getName().toUpperCase())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16)
                    .setFontColor(ColorConstants.YELLOW)
                    .setMarginBottom(10));

            // 📅 Infos principales sous forme de tableau
            Table table = new Table(2).useAllAvailableWidth();
            table.addCell(createStyledCell("📅 Date :", true));
            table.addCell(createStyledCell(selectedSeance.getFilm().getDate(), false));
            table.addCell(createStyledCell("🕒 Séance :", true));
            table.addCell(createStyledCell("Numéro " + selectedSeance.getNumSeance(), false));
            table.addCell(createStyledCell("🎟 Prix :", true));
            table.addCell(createStyledCell(selectedSeance.getPrix() + " €", false));
            table.addCell(createStyledCell("👤 Client :", true));
            table.addCell(createStyledCell(selectedSeance.getClient().getNomC() + " " + selectedSeance.getClient().getPrenom(), false));

            document.add(table);

            // 🔗 QR Code pour validation


            // ✂️ Bordure perforée
            canvas.setStrokeColor(ColorConstants.LIGHT_GRAY)
                    .setLineWidth(1f)
                    .setLineDash(4, 4)  // Effet de pointillé
                    .moveTo(20, 120).lineTo(280, 120)
                    .stroke();

            // 🔥 Message final
            document.add(new Paragraph("🎬 Bon film !")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setBold()
                    .setFontColor(ColorConstants.WHITE)
                    .setMarginTop(10));

            document.close();
            showAlert("Succès", "Le ticket a été généré : " + filePath);

            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                java.awt.Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 📌 Fonction pour styliser les cellules du tableau
    private Cell createStyledCell(String text, boolean isHeader) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFontSize(10).setFontColor(ColorConstants.WHITE))
                .setPadding(5)
                .setBorder(Border.NO_BORDER);

        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.DARK_GRAY).setBold();
        }

        return cell;
    }

}