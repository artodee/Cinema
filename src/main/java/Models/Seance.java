package Models;

public class Seance {
    private int id;
    private int  numSeance;
    private Film film;
    private Client client;
    private double prix;

    public Seance(int id, int numSeance, Film film, Client client, double prix) {
        this.id = id;
        this.numSeance = numSeance;
        this.film = film;
        this.client = client;
        this.prix = prix;
    }


    public int getId() { return id; }
    public int getNumSeance() { return numSeance; }
    public Film getFilm() { return film; }
    public Client getClient() { return client; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Séance " + numSeance + " | " +
                "Client: " + client.getNomC() + " " + client.getPrenom() + " | " +
                "Film: " + film.getName() + " | " +
                "Prix: " + prix + "€";
    }
}