package tec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests unitaires pour EtatPassager.
 * 
 * Ces tests assurent une couverture complète de la classe EtatPassager.
 */
public class EtatPassagerTest {

    // ========================================================================
    // TESTS DE CONSTRUCTION
    // ========================================================================

    @Test
    public void testConstructeurDehors() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        
        assertTrue(etat.estExterieur());
        assertFalse(etat.estAssis());
        assertFalse(etat.estDebout());
        assertFalse(etat.estInterieur());
    }

    @Test
    public void testConstructeurAssis() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.ASSIS);
        
        assertFalse(etat.estExterieur());
        assertTrue(etat.estAssis());
        assertFalse(etat.estDebout());
        assertTrue(etat.estInterieur());
    }

    @Test
    public void testConstructeurDebout() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEBOUT);
        
        assertFalse(etat.estExterieur());
        assertFalse(etat.estAssis());
        assertTrue(etat.estDebout());
        assertTrue(etat.estInterieur());
    }

    // ========================================================================
    // TESTS DE CHANGEMENT D'ÉTAT
    // ========================================================================

    @Test
    public void testChangerEnDehors() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.ASSIS);
        IEtatPassager nouvelEtat = etat.changerEnDehors();
        
        assertTrue(nouvelEtat.estExterieur());
        assertFalse(nouvelEtat.estAssis());
        assertFalse(nouvelEtat.estDebout());
    }

    @Test
    public void testChangerEnAssis() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        IEtatPassager nouvelEtat = etat.changerEnAssis();
        
        assertFalse(nouvelEtat.estExterieur());
        assertTrue(nouvelEtat.estAssis());
        assertFalse(nouvelEtat.estDebout());
    }

    @Test
    public void testChangerEnDebout() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        IEtatPassager nouvelEtat = etat.changerEnDebout();
        
        assertFalse(nouvelEtat.estExterieur());
        assertFalse(nouvelEtat.estAssis());
        assertTrue(nouvelEtat.estDebout());
    }

    // ========================================================================
    // TESTS DE TRANSITION
    // ========================================================================

    @Test
    public void testTransitionDehorsVersAssis() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        IEtatPassager assisEtat = etat.changerEnAssis();
        
        assertTrue(assisEtat.estAssis());
        assertTrue(assisEtat.estInterieur());
    }

    @Test
    public void testTransitionDehorsVersDebout() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        IEtatPassager deboutEtat = etat.changerEnDebout();
        
        assertTrue(deboutEtat.estDebout());
        assertTrue(deboutEtat.estInterieur());
    }

    @Test
    public void testTransitionAssisVersDehors() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.ASSIS);
        IEtatPassager dehorsEtat = etat.changerEnDehors();
        
        assertTrue(dehorsEtat.estExterieur());
        assertFalse(dehorsEtat.estInterieur());
    }

    @Test
    public void testTransitionDeboutVersDehors() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEBOUT);
        IEtatPassager dehorsEtat = etat.changerEnDehors();
        
        assertTrue(dehorsEtat.estExterieur());
        assertFalse(dehorsEtat.estInterieur());
    }

    // ========================================================================
    // TESTS DE toString
    // ========================================================================

    @Test
    public void testToStringDehors() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        assertTrue(etat.toString().contains("dehors"));
    }

    @Test
    public void testToStringAssis() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.ASSIS);
        assertTrue(etat.toString().contains("assis"));
    }

    @Test
    public void testToStringDebout() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEBOUT);
        assertTrue(etat.toString().contains("debout"));
    }

    // ========================================================================
    // TESTS D'IMMUTABILITÉ
    // ========================================================================

    @Test
    public void testImmutabiliteChangerEtat() {
        EtatPassager etatOriginal = new EtatPassager(EtatPassager.Etat.DEHORS);
        IEtatPassager nouvelEtat = etatOriginal.changerEnAssis();
        
        // L'état original ne doit pas être modifié
        assertTrue(etatOriginal.estExterieur());
        assertFalse(etatOriginal.estAssis());
        
        // Le nouvel état est différent
        assertTrue(nouvelEtat.estAssis());
    }

    @Test
    public void testChaineDeTransitions() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        
        IEtatPassager assis = etat.changerEnAssis();
        IEtatPassager dehors = assis.changerEnDehors();
        IEtatPassager debout = dehors.changerEnDebout();
        IEtatPassager dehors2 = debout.changerEnDehors();
        
        assertTrue(dehors2.estExterieur());
    }
}
