package tec;

/**
 * Interface publique définissant un transport.
 * 
 * Un transport voyage d'arrêt en arrêt et prévient ses passagers
 * de chaque nouvel arrêt.
 */
public interface Transport {

    /**
     * Indique au transport de simuler l'arrêt suivant.
     * 
     * @throws UsagerInvalideException si l'état d'un usager est incohérent
     */
    void allerArretSuivant() throws UsagerInvalideException;
}
