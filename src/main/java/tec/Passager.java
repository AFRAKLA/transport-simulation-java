package tec;

/**
 * Interface privée définissant les interactions d'un passager avec le bus.
 * 
 * Un passager possède un nom et trois états possibles : dehors, assis, debout.
 * Les méthodes accepter*() changent l'état du passager.
 */
public interface Passager {

    /**
     * Retourne le nom du passager.
     * @return le nom du passager
     */
    String nom();

    /**
     * Vérifie si le passager est hors du bus.
     * @return true si le passager est dehors
     */
    boolean estDehors();

    /**
     * Vérifie si le passager est assis dans le bus.
     * @return true si le passager est assis
     */
    boolean estAssis();

    /**
     * Vérifie si le passager est debout dans le bus.
     * @return true si le passager est debout
     */
    boolean estDebout();

    /**
     * Change l'état du passager en "hors du bus".
     * Cette méthode est appelée par un objet Bus.
     */
    void accepterSortie();

    /**
     * Change l'état du passager en "assis".
     * Cette méthode est appelée par un objet Bus.
     */
    void accepterPlaceAssise();

    /**
     * Change l'état du passager en "debout".
     * Cette méthode est appelée par un objet Bus.
     */
    void accepterPlaceDebout();

    /**
     * Appelée à chaque nouvel arrêt pour permettre au passager de réagir.
     * 
     * @param bus le bus dans lequel se trouve le passager
     * @param numeroArret le numéro de l'arrêt actuel
     */
    void nouvelArret(Bus bus, int numeroArret);
}
