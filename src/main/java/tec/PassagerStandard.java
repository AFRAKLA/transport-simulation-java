package tec;

/**
 * Classe PassagerStandard.
 * 
 * Cette classe représente un passager classique utilisant un moyen
 * de transport de type bus.
 * 
 * Le passager possède un nom, une destination et un état
 * (dehors, assis ou debout).
 * 
 * Elle implémente les interfaces Usager et Passager.
 */
public class PassagerStandard implements Usager, Passager {

    /** Nom du passager */
    private String nom;

    /** Arrêt de destination du passager */
    private int destination;

    /** État courant du passager */
    private EtatPassager etat;

    /**
     * Construit un passager standard.
     * Le passager est initialement dehors.
     * 
     * @param nom nom du passager
     * @param destination arrêt de destination
     * @throws IllegalArgumentException si le nom est null ou si la destination est négative
     */
    public PassagerStandard(String nom, int destination) {
        if (nom == null) {
            throw new IllegalArgumentException("Le nom ne peut pas etre null");
        }
        if (destination < 0) {
            throw new IllegalArgumentException("La destination ne peut pas etre négative");
        }
        this.nom = nom;
        this.destination = destination;
        this.etat = new EtatPassager(EtatPassager.Etat.DEHORS);
    }

    /**
     * Construit un passager standard en précisant l'état initial.
     * 
     * Ce constructeur est principalement utilisé pour les tests.
     * 
     * @param nom nom du passager
     * @param destination arrêt de destination
     * @param etat état initial du passager
     * @throws IllegalArgumentException si un des paramètres est invalide
     */
    public PassagerStandard(String nom, int destination, EtatPassager etat) {
        if (nom == null) {
            throw new IllegalArgumentException("Le nom ne peut pas etre null");
        }
        if (destination < 0) {
            throw new IllegalArgumentException("La destination ne peut pas etre négative");
        }
        if (etat == null) {
            throw new IllegalArgumentException("L'état ne peut pas etre null");
        }
        this.nom = nom;
        this.destination = destination;
        this.etat = etat;
    }

    /**
     * Construit un passager standard avec un nom par défaut.
     * 
     * @param destination arrêt de destination
     */
    public PassagerStandard(int destination) {
        this("PassagerStandard" + destination, destination);
    }

    /**
     * Indique si le passager est à l'extérieur du bus.
     * 
     * @return true si le passager est dehors, false sinon
     */
    @Override
    public boolean estDehors() {
        return etat.estExterieur();
    }

    /**
     * Indique si le passager est assis.
     * 
     * @return true si le passager est assis, false sinon
     */
    @Override
    public boolean estAssis() {
        return etat.estAssis();
    }

    /**
     * Indique si le passager est debout.
     * 
     * @return true si le passager est debout, false sinon
     */
    @Override
    public boolean estDebout() {
        return etat.estDebout();
    }

    /**
     * Fait sortir le passager du bus.
     * 
     * @throws IllegalStateException si le passager est déjà dehors
     */
    @Override
    public void accepterSortie() {
        if (estDehors()) {
            throw new IllegalStateException("Le passager est déja dehors");
        }
        etat = (EtatPassager) etat.changerEnDehors();
    }

    /**
     * Fait asseoir le passager dans le bus.
     * 
     * @throws IllegalStateException si le passager n'est pas dehors
     */
    @Override
    public void accepterPlaceAssise() {
        if (!estDehors()) {
            throw new IllegalStateException("Le passager n'est pas dehors");
        }
        etat = (EtatPassager) etat.changerEnAssis();
    }

    /**
     * Fait mettre le passager debout dans le bus.
     * 
     * @throws IllegalStateException si le passager n'est pas dehors
     */
    @Override
    public void accepterPlaceDebout() {
        if (!estDehors()) {
            throw new IllegalStateException("Le passager n'est pas dehors");
        }
        etat = (EtatPassager) etat.changerEnDebout();
    }

    /**
     * Méthode appelée lors du passage à un nouvel arrêt.
     * 
     * Si le passager arrive à sa destination, il demande sa sortie.
     * 
     * @param bus bus dans lequel se trouve le passager
     * @param numeroArret numéro de l'arrêt courant
     * @throws IllegalArgumentException si le bus est null ou si le numéro d'arrêt est négatif
     */
    @Override
    public void nouvelArret(Bus bus, int numeroArret) {
        if (bus == null) {
            throw new IllegalArgumentException("Le bus ne peut pas etre null");
        }
        if (numeroArret < 0) {
            throw new IllegalArgumentException("Le numéro d'arret ne peut pas etre négatif");
        }
        if (!estDehors() && numeroArret == destination) {
            bus.demanderSortie(this);
        }
    }

    /**
     * Retourne le nom du passager.
     * 
     * @return nom du passager
     */
    @Override
    public String nom() {
        return nom;
    }

    /**
     * Permet au passager de monter dans un transport.
     * 
     * @param t transport utilisé
     * @throws UsagerInvalideException si le transport est null
     *         ou si aucune place n'est disponible
     */
    @Override
    public void monterDans(Transport t) throws UsagerInvalideException {
        if (t == null) {
            throw new UsagerInvalideException("Transport null", this, null);
        }

        Bus bus = (Bus) t;

        if (!bus.aPlaceAssise() && !bus.aPlaceDebout()) {
            throw new UsagerInvalideException("Aucune place disponible", this, t);
        }

        if (bus.aPlaceAssise()) {
            bus.demanderPlaceAssise(this);
        } else {
            // Si on arrive ici, c'est que bus.aPlaceDebout() est forcément true
            // car sinon l'exception ci-dessus aurait été levée
            bus.demanderPlaceDebout(this);
        }
    }

    /**
     * Retourne la destination du passager.
     * 
     * @return numéro de l'arrêt de destination
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Retourne une représentation textuelle du passager.
     * 
     * @return chaîne de caractères représentant le passager
     */
    @Override
    public String toString() {
        return nom + etat;
    }
}
