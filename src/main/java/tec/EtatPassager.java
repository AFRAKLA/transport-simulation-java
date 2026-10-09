package tec;

/**
 * Implémentation de IEtatPassager qui utilise une énumération (enum).
 * 
 * Cette classe représente l'état d'un passager dans le système de transport.
 */
public class EtatPassager implements IEtatPassager {
    
    /**
     * Définit les trois états possibles pour un passager.
     */
    public enum Etat { 
        ASSIS, 
        DEBOUT, 
        DEHORS 
    }
    
    private Etat monEtat;

    /**
     * Construit un passager avec un état initial.
     * @param e l'état initial du passager
     */
    public EtatPassager(Etat e) {
        monEtat = e;
    }

    /**
     * Vérifie si le passager est dehors.
     * @return true si le passager est dehors
     */
    @Override
    public boolean estExterieur() {
        return monEtat == Etat.DEHORS;
    }

    /**
     * Vérifie si le passager est assis.
     * @return true si le passager est assis
     */
    @Override
    public boolean estAssis() {
        return monEtat == Etat.ASSIS;
    }

    /**
     * Vérifie si le passager est debout.
     * @return true si le passager est debout
     */
    @Override
    public boolean estDebout() {
        return monEtat == Etat.DEBOUT;
    }

    /**
     * Vérifie si le passager est à l'intérieur (assis ou debout).
     * @return true si le passager est à l'intérieur
     */
    @Override
    public boolean estInterieur() {
        return estAssis() || estDebout();
    }

    /**
     * Crée un nouvel objet passager à l'état "dehors".
     * @return un nouvel état passager "dehors"
     */
    @Override
    public IEtatPassager changerEnDehors() {
        return new EtatPassager(Etat.DEHORS);
    }

    /**
     * Crée un nouvel objet passager à l'état "assis".
     * @return un nouvel état passager "assis"
     */
    @Override
    public IEtatPassager changerEnAssis() {
        return new EtatPassager(Etat.ASSIS);
    }

    /**
     * Crée un nouvel objet passager à l'état "debout".
     * @return un nouvel état passager "debout"
     */
    @Override
    public IEtatPassager changerEnDebout() {
        return new EtatPassager(Etat.DEBOUT);
    }

    /**
     * Fournit une représentation textuelle de l'objet.
     * @return l'état en minuscules précédé d'un espace
     */
    @Override
    public String toString() {
        return " " + monEtat.toString().toLowerCase();
    }
}
