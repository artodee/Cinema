package Models;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Film {

    private String date;

    private String genre;

    private String nom;

    private int id;

    public Film(String date, String genre, String nom, int id) {
        this.date = date;
        this.genre = genre;
        this.nom = nom;
    }

    public String getDate() {return date;}

    public int getId() {return id;}

    public String getGenre() {
        return genre;
    }

    public String getName(){return nom;}

    @Override
    public String toString() {
        return date + " - " + genre + " - " + nom;
    }

}
