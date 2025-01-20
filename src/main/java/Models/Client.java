package Models;

import com.jfoenix.controls.JFXListView;
import javafx.scene.control.ListView;

public class Client {

    private final int id;
    private String nomC;
    private String prenom;
    private String mail;
    private ListView clientList;


    // Constructeur
    public Client(int id, String nomC, String prenom, String mail, ListView clientList) {
        this.id = id;
        this.nomC = nomC;
        this.prenom = prenom;
        this.mail = mail;
        this.clientList = clientList;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // Méthode pour afficher un client dans une ListView ou autre composant graphique
    @Override
    public String toString() {
        return nomC + " " + prenom + " (" + mail + ")";
    }
}