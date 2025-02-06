package Models;

public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private String password;

    public Utilisateur(int id, String nomUtilisateur, String prenomUtilisateur, String password) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.prenomUtilisateur = prenomUtilisateur;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nomUtilisateur;
    }

    public String getPrenom() {
        return prenomUtilisateur;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return prenomUtilisateur + " " + nomUtilisateur; // Cela permet d'afficher quelque chose de lisible dans la ListView
    }
}