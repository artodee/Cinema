package Models;

public class Film {

    private String date;

    private String genre;

    private String nom;

    private int id;

    public Film(String nom, String genre, String date, int id) {
        this.date = date;
        this.genre = genre;
        this.nom = nom;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDate() {return date;}


    public String getGenre() {
        return genre;
    }

    public String getName(){return nom;}

    @Override
    public String toString() {
        return nom + " - " + genre + " - " + date;
    }

}
