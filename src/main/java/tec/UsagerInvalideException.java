package tec;

/**
 * Exception levée lorsqu'un usager tente une action invalide.
 * 
 * Cette exception est utilisée pour signaler des erreurs liées
 * aux passagers dans le système de transport.
 */
public class UsagerInvalideException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * L'usager concerné par l'exception.
     */
    public final Usager quelUsager;

    /**
     * Le transport concerné par l'exception.
     */
    public final Transport quelTransport;

    /**
     * Construit une exception avec un message.
     * 
     * @param message le message d'erreur
     */
    public UsagerInvalideException(String message) {
        super(message);
        this.quelUsager = null;
        this.quelTransport = null;
    }

    /**
     * Construit une exception avec un message, l'usager et le transport concernés.
     * 
     * @param message le message d'erreur
     * @param usager l'usager concerné
     * @param transport le transport concerné
     */
    public UsagerInvalideException(String message, Usager usager, Transport transport) {
        super(message);
        this.quelUsager = usager;
        this.quelTransport = transport;
    }

    /**
     * Retourne le message d'erreur.
     * @return le message d'erreur
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
