package tec;

/**
 * Classe JaugeNaturel.
 * 
 * Cette classe représente une jauge avec un niveau borné entre
 * une valeur minimale et une valeur maximale.
 */
public class JaugeNaturel implements IJauge {

    private int min;
    private int max;
    private int niveau;

    /**
     * Construit une jauge naturelle.
     * 
     * @param min valeur minimale de la jauge
     * @param max valeur maximale de la jauge
     * @param niveau niveau initial de la jauge
     * @throws IllegalArgumentException si min > max ou si niveau hors bornes
     */
    public JaugeNaturel(int min, int max, int niveau) {
        if (min > max) {
            throw new IllegalArgumentException("Le minimum ne peut pas être supérieur au maximum");
        }
        if (niveau < min || niveau > max) {
            throw new IllegalArgumentException("Le niveau doit être compris entre min et max");
        }
        this.min = min;
        this.max = max;
        this.niveau = niveau;
    }

    /**
     * Vérifie si la jauge est au minimum (niveau vert).
     * @return true si le niveau est égal au minimum
     */
    @Override
    public boolean estVert() {
        return niveau == min;
    }

    /**
     * Vérifie si la jauge est au maximum (niveau rouge).
     * @return true si le niveau est égal au maximum
     */
    @Override
    public boolean estRouge() {
        return niveau == max;
    }

    /**
     * Incrémente le niveau de la jauge.
     * Ne fait rien si le niveau est déjà au maximum.
     */
    @Override
    public void incrementer() {
        if (niveau < max) {
            niveau++;
        }
    }

    /**
     * Décrémente le niveau de la jauge.
     * Ne fait rien si le niveau est déjà au minimum.
     */
    @Override
    public void decrementer() {
        if (niveau > min) {
            niveau--;
        }
    }

    /**
     * Retourne le niveau actuel de la jauge.
     * @return le niveau courant
     */
    @Override
    public int getNiveau() {
        return niveau;
    }

    /**
     * Représentation textuelle de la jauge.
     * @return chaîne décrivant l'état de la jauge
     */
    @Override
    public String toString() {
        return "[min:" + min + ", max:" + max + ", niveau:" + niveau + "]";
    }
}
