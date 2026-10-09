package tec;

/**
 * Factory pour créer les objets Transport et Usager.
 * 
 * Cette classe permet d'instancier des Autobus et des PassagerStandard
 * de manière uniforme sans exposer les classes concrètes.
 */
public class TransportFactory {

    /**
     * Crée un autobus avec le nombre de places spécifié.
     * 
     * @param nbAssis nombre de places assises
     * @param nbDebout nombre de places debout
     * @return un nouveau Transport (Autobus)
     */
    public static Transport creerAutobus(int nbAssis, int nbDebout) {
        return new Autobus(nbAssis, nbDebout);
    }

    /**
     * Crée un autobus avec le même nombre de places assises et debout.
     * 
     * @param nbPlace nombre de places (assises et debout)
     * @return un nouveau Transport (Autobus)
     */
    public static Transport creerAutobus(int nbPlace) {
        return new Autobus(nbPlace);
    }

    /**
     * Crée un passager standard.
     * 
     * @param nom nom du passager
     * @param destination arrêt de destination
     * @return un nouvel Usager (PassagerStandard)
     */
    public static Usager creerPassager(String nom, int destination) {
        return new PassagerStandard(nom, destination);
    }

    /**
     * Crée un passager standard avec un nom par défaut.
     * 
     * @param destination arrêt de destination
     * @return un nouvel Usager (PassagerStandard)
     */
    public static Usager creerPassager(int destination) {
        return new PassagerStandard(destination);
    }
}
