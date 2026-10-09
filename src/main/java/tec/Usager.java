package tec;

/**
 * Interface publique définissant un usager du transport.
 * 
 * Un usager peut monter dans un transport et a un nom.
 */
public interface Usager {

    /**
     * Retourne le nom de l'usager.
     * @return le nom de l'usager
     */
    String nom();

    /**
     * Vérifie si l'usager est dehors (hors du bus).
     * @return true si l'usager est dehors
     */
    boolean estDehors();

    /**
     * Vérifie si l'usager est assis.
     * @return true si l'usager est assis
     */
    boolean estAssis();

    /**
     * Vérifie si l'usager est debout.
     * @return true si l'usager est debout
     */
    boolean estDebout();

    /**
     * Permet à l'usager de monter dans un transport.
     * 
     * @param t le transport dans lequel monter
     * @throws UsagerInvalideException si l'usager ne peut pas monter
     */
    void monterDans(Transport t) throws UsagerInvalideException;
}
