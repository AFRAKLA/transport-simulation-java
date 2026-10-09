package tec;

/**
 * Classe Autobus.
 * 
 * Cette classe représente un autobus qui permet de gérer des passagers
 * assis ou debout, en respectant une capacité maximale.
 * 
 * Elle implémente les interfaces Transport et Bus.
 * 
 * Elle gère également les changements d'état des passagers
 * (assis, debout, dehors) ainsi que le passage aux arrêts suivants.
 */
public class Autobus implements Transport, Bus {

    private JaugeNaturel jaugeAssis;
    private JaugeNaturel jaugeDebout;
    private Passager[] passagers;
    private int numeroArret;

    
    /**
     * Construit un autobus avec un nombre de places assises et debout.
     * 
     * @param nbAssis nombre de places assises
     * @param nbDebout nombre de places debout
     * @throws UsagerInvalideException si un des paramètres est négatif
     */
    public Autobus(int nbAssis, int nbDebout) {
        if (nbAssis < 0) {
            throw new UsagerInvalideException("Le nombre de places assises ne peut pas être négatif: " + nbAssis);
        }
        if (nbDebout < 0) {
            throw new UsagerInvalideException("Le nombre de places debout ne peut pas être négatif: " + nbDebout);
        }
        this.jaugeAssis = new JaugeNaturel(0, nbAssis, 0);
        this.jaugeDebout = new JaugeNaturel(0, nbDebout, 0);
        this.passagers = new Passager[nbAssis + nbDebout];
        this.numeroArret = 0;
    }

    /**
     * Construit un autobus avec le même nombre de places assises et debout.
     * 
     * @param nbPlace nombre de places assises et debout
     */
    public Autobus(int nbPlace) {
        this(nbPlace, nbPlace);
    }

    /**
     * Ajoute un passager dans le tableau des passagers.
     * 
     * @param p passager à ajouter
     */
    private void ajouterPassager(Passager p) {
        for (int i = 0; i < passagers.length; i++) {
            if (passagers[i] == null) {
                passagers[i] = p;
                break;
            }
        }
    }

    /**
     * Retire un passager du tableau des passagers.
     * 
     * @param p passager à retirer
     */
    private void retirerPassager(Passager p) {
        for (int i = 0; i < passagers.length; i++) {
            if (passagers[i] == p) {
                passagers[i] = null;
                break;
            }
        }
    }

    /**
     * Indique s'il reste des places assises disponibles.
     * 
     * @return true s'il reste une place assise, false sinon
     */
    @Override
    public boolean aPlaceAssise() {
        return !jaugeAssis.estRouge();
    }

    /**
     * Indique s'il reste des places debout disponibles.
     * 
     * @return true s'il reste une place debout, false sinon
     */
    @Override
    public boolean aPlaceDebout() {
        return !jaugeDebout.estRouge();
    }

    /**
     * Demande une place assise pour un passager.
     * 
     * @param p passager concerné
     * @throws UsagerInvalideException si le passager est null
     */
    @Override
    public void demanderPlaceAssise(Passager p) {
        if (p == null) {
            throw new UsagerInvalideException("Passager null");
        }
        if (p.estDehors() && aPlaceAssise()) {
            p.accepterPlaceAssise();
            jaugeAssis.incrementer();
            ajouterPassager(p);
        }
    }

    /**
     * Demande une place debout pour un passager.
     * 
     * @param p passager concerné
     * @throws UsagerInvalideException si le passager est null
     */
    @Override
    public void demanderPlaceDebout(Passager p) {
        if (p == null) {
            throw new UsagerInvalideException("Passager null");
        }
        if (p.estDehors() && aPlaceDebout()) {
            p.accepterPlaceDebout();
            jaugeDebout.incrementer();
            ajouterPassager(p);
        }
    }

    /**
     * Demande à un passager assis de passer debout.
     * 
     * @param p passager concerné
     */
    @Override
    public void demanderChangerEnDebout(Passager p) {
        if (p.estAssis() && aPlaceDebout()) {
            p.accepterSortie();
            p.accepterPlaceDebout();
            jaugeAssis.decrementer();
            jaugeDebout.incrementer();
        }
    }

    /**
     * Demande à un passager debout de passer assis.
     * 
     * @param p passager concerné
     */
    @Override
    public void demanderChangerEnAssis(Passager p) {
        if (p.estDebout() && aPlaceAssise()) {
            p.accepterSortie();
            p.accepterPlaceAssise();
            jaugeDebout.decrementer();
            jaugeAssis.incrementer();
        }
    }

    /**
     * Demande la sortie d'un passager.
     * 
     * @param p passager concerné
     * @throws UsagerInvalideException si le passager est null
     */
    @Override
    public void demanderSortie(Passager p) {
        if (p == null) {
            throw new UsagerInvalideException("Passager null");
        }
        if (p.estAssis()) {
            jaugeAssis.decrementer();
        } else if (p.estDebout()) {
            jaugeDebout.decrementer();
        }
        p.accepterSortie();
        retirerPassager(p);
    }

    /**
     * Fait avancer l'autobus à l'arrêt suivant
     * et informe tous les passagers présents.
     * 
     * @throws UsagerInvalideException en cas d'erreur liée aux passagers
     */
    @Override
    public void allerArretSuivant() throws UsagerInvalideException {
        numeroArret++;
        for (Passager p : passagers) {
            if (p != null) {
                p.nouvelArret(this, numeroArret);
            }
        }
    }

    /**
     * Retourne le numéro de l'arrêt actuel.
     * 
     * @return le numéro de l'arrêt
     */
    public int getNumeroArret() {
        return numeroArret;
    }

    /**
     * Retourne le nombre de passagers assis.
     * 
     * @return le nombre de passagers assis
     */
    public int getNbAssis() {
        return jaugeAssis.getNiveau();
    }

    /**
     * Retourne le nombre de passagers debout.
     * 
     * @return le nombre de passagers debout
     */
    public int getNbDebout() {
        return jaugeDebout.getNiveau();
    }

    /**
     * Retourne une représentation textuelle de l'état de l'autobus.
     * 
     * @return chaîne de caractères décrivant l'autobus
     */
    @Override
    public String toString() {
        return "[arret:" + numeroArret + ", assis:" + jaugeAssis.getNiveau() + ", debout:" + jaugeDebout.getNiveau() + "]";
    }
}
