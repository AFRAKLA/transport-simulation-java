package tec;

/**
 * Interface définissant l'état d'un passager.
 * 
 * Un passager peut être dans trois états : dehors, assis ou debout.
 * Cette interface permet de changer l'état d'un passager.
 */
public interface IEtatPassager {

    /**
     * Vérifie si le passager est à l'extérieur du bus.
     * @return true si le passager est dehors
     */
    boolean estExterieur();

    /**
     * Vérifie si le passager est assis.
     * @return true si le passager est assis
     */
    boolean estAssis();

    /**
     * Vérifie si le passager est debout.
     * @return true si le passager est debout
     */
    boolean estDebout();

    /**
     * Vérifie si le passager est à l'intérieur du bus (assis ou debout).
     * @return true si le passager est à l'intérieur
     */
    boolean estInterieur();

    /**
     * Change l'état du passager en "dehors".
     * @return un nouvel état passager "dehors"
     */
    IEtatPassager changerEnDehors();

    /**
     * Change l'état du passager en "assis".
     * @return un nouvel état passager "assis"
     */
    IEtatPassager changerEnAssis();

    /**
     * Change l'état du passager en "debout".
     * @return un nouvel état passager "debout"
     */
    IEtatPassager changerEnDebout();
}
