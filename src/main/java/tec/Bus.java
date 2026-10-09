package tec;

/**
 * Interface privée définissant les interactions d'un bus avec les passagers.
 * 
 * Un bus a un nombre déterminé de places assises et debout.
 * Il autorise les passagers à entrer et à changer de place.
 */
public interface Bus {

    /**
     * Vérifie s'il existe des places assises disponibles.
     * @return true s'il existe des places assises
     */
    boolean aPlaceAssise();

    /**
     * Vérifie s'il existe des places debout disponibles.
     * @return true s'il existe des places debout
     */
    boolean aPlaceDebout();

    /**
     * Le passager entre dans le bus en demandant une place assise.
     * L'état du passager doit être "dehors".
     * 
     * @param p le passager
     */
    void demanderPlaceAssise(Passager p);

    /**
     * Le passager entre dans le bus en demandant une place debout.
     * L'état du passager doit être "dehors".
     * 
     * @param p le passager
     */
    void demanderPlaceDebout(Passager p);

    /**
     * Le passager demande à changer de place assise vers debout.
     * 
     * @param p le passager
     */
    void demanderChangerEnDebout(Passager p);

    /**
     * Le passager demande à changer de place debout vers assise.
     * 
     * @param p le passager
     */
    void demanderChangerEnAssis(Passager p);

    /**
     * Le passager demande à sortir du bus.
     * 
     * @param p le passager
     */
    void demanderSortie(Passager p);
}
