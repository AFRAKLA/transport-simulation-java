package tec;

/**
 * Interface définissant une jauge avec un niveau borné.
 */
public interface IJauge {

    /**
     * Vérifie si la jauge est au minimum (niveau vert).
     * @return true si le niveau est au minimum
     */
    boolean estVert();

    /**
     * Vérifie si la jauge est au maximum (niveau rouge).
     * @return true si le niveau est au maximum
     */
    boolean estRouge();

    /**
     * Incrémente le niveau de la jauge.
     */
    void incrementer();

    /**
     * Décrémente le niveau de la jauge.
     */
    void decrementer();

    /**
     * Retourne le niveau actuel de la jauge.
     * @return le niveau courant
     */
    int getNiveau();
}
