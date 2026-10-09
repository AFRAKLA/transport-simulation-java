package tec;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests complémentaires pour assurer une couverture complète des branches.
 * 
 * Ce fichier contient les tests pour les chemins d'exécution non couverts
 * par les autres fichiers de tests.
 */
public class CouvertureBranchesTest {

    private Autobus bus;

    @Before
    public void setUp() {
        bus = new Autobus(3, 5);
    }

    // ========================================================================
    // PASSAGER STANDARD - Branches manquantes
    // ========================================================================

    /**
     * Test du constructeur avec 3 paramètres et destination négative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructeur3ParamsDestinationNegative() {
        new PassagerStandard("Test", -1, new EtatPassager(EtatPassager.Etat.DEHORS));
    }

    /**
     * Test du constructeur avec 3 paramètres et nom null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructeur3ParamsNomNull() {
        new PassagerStandard(null, 5, new EtatPassager(EtatPassager.Etat.DEHORS));
    }

    /**
     * Test nouvelArret avec passager debout arrivant à destination.
     */
    @Test
    public void testNouvelArretPassagerDeboutADestination() {
        Bus mockBus = mock(Bus.class);
        PassagerStandard p = new PassagerStandard("Test", 3, new EtatPassager(EtatPassager.Etat.DEBOUT));
        
        doAnswer(invocation -> {
            Passager passager = invocation.getArgument(0);
            passager.accepterSortie();
            return null;
        }).when(mockBus).demanderSortie(any(Passager.class));
        
        // Act - Passager debout à destination
        p.nouvelArret(mockBus, 3);
        
        // Assert
        verify(mockBus).demanderSortie(p);
        assertTrue("Le passager debout devrait être sorti", p.estDehors());
    }

    /**
     * Test nouvelArret avec passager assis pas encore à destination.
     */
    @Test
    public void testNouvelArretPassagerAssisPasADestination() {
        Bus mockBus = mock(Bus.class);
        PassagerStandard p = new PassagerStandard("Test", 10, new EtatPassager(EtatPassager.Etat.ASSIS));
        
        // Act
        p.nouvelArret(mockBus, 5);
        
        // Assert - Ne doit pas demander à sortir
        verify(mockBus, never()).demanderSortie(any());
        assertTrue("Le passager devrait rester assis", p.estAssis());
    }

    /**
     * Test nouvelArret avec passager debout pas encore à destination.
     */
    @Test
    public void testNouvelArretPassagerDeboutPasADestination() {
        Bus mockBus = mock(Bus.class);
        PassagerStandard p = new PassagerStandard("Test", 10, new EtatPassager(EtatPassager.Etat.DEBOUT));
        
        // Act
        p.nouvelArret(mockBus, 5);
        
        // Assert
        verify(mockBus, never()).demanderSortie(any());
        assertTrue("Le passager devrait rester debout", p.estDebout());
    }

    // ========================================================================
    // AUTOBUS - Branches manquantes
    // ========================================================================

    /**
     * Test demanderChangerEnDebout avec passager debout (pas assis).
     * La branche p.estAssis() est false.
     */
    @Test
    public void testDemanderChangerEnDeboutPassagerDebout() {
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceDebout(mockPassager);
        
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(false);
        when(mockPassager.estDebout()).thenReturn(true);
        
        // Act
        bus.demanderChangerEnDebout(mockPassager);
        
        // Assert - Pas de changement car pas assis
        verify(mockPassager, never()).accepterSortie();
        verify(mockPassager, never()).accepterPlaceDebout();
    }

    /**
     * Test demanderChangerEnDebout avec passager dehors.
     */
    @Test
    public void testDemanderChangerEnDeboutPassagerDehors() {
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estAssis()).thenReturn(false);
        when(mockPassager.estDebout()).thenReturn(false);
        when(mockPassager.estDehors()).thenReturn(true);
        
        // Act
        bus.demanderChangerEnDebout(mockPassager);
        
        // Assert - Pas de changement car pas assis
        verify(mockPassager, never()).accepterSortie();
        verify(mockPassager, never()).accepterPlaceDebout();
    }

    /**
     * Test demanderChangerEnAssis avec passager assis (pas debout).
     * La branche p.estDebout() est false.
     */
    @Test
    public void testDemanderChangerEnAssisPassagerAssis() {
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceAssise(mockPassager);
        
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(true);
        when(mockPassager.estDebout()).thenReturn(false);
        
        // Act
        bus.demanderChangerEnAssis(mockPassager);
        
        // Assert - Pas de changement car pas debout
        verify(mockPassager, never()).accepterSortie();
        verify(mockPassager, never()).accepterPlaceAssise();
    }

    /**
     * Test demanderChangerEnAssis avec passager dehors.
     */
    @Test
    public void testDemanderChangerEnAssisPassagerDehors() {
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estAssis()).thenReturn(false);
        when(mockPassager.estDebout()).thenReturn(false);
        when(mockPassager.estDehors()).thenReturn(true);
        
        // Act
        bus.demanderChangerEnAssis(mockPassager);
        
        // Assert - Pas de changement car pas debout
        verify(mockPassager, never()).accepterSortie();
        verify(mockPassager, never()).accepterPlaceAssise();
    }

    /**
     * Test demanderSortie avec passager déjà dehors (ni assis ni debout).
     */
    @Test
    public void testDemanderSortiePassagerDejaDehorsMock() {
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estAssis()).thenReturn(false);
        when(mockPassager.estDebout()).thenReturn(false);
        
        // Act
        bus.demanderSortie(mockPassager);
        
        // Assert - accepterSortie appelé mais pas de décrémentation des jauges
        verify(mockPassager).accepterSortie();
        assertEquals(0, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
    }

    /**
     * Test demanderPlaceDebout quand bus plein de debout.
     */
    @Test
    public void testDemanderPlaceDeboutBusPlein() {
        Autobus busPetit = new Autobus(2, 1); // 1 place debout
        
        Passager mockPassager1 = mock(Passager.class);
        when(mockPassager1.estDehors()).thenReturn(true);
        busPetit.demanderPlaceDebout(mockPassager1);
        
        assertFalse("Plus de places debout", busPetit.aPlaceDebout());
        
        Passager mockPassager2 = mock(Passager.class);
        when(mockPassager2.estDehors()).thenReturn(true);
        
        // Act
        busPetit.demanderPlaceDebout(mockPassager2);
        
        // Assert - Pas d'acceptation car plus de place
        verify(mockPassager2, never()).accepterPlaceDebout();
    }

    // ========================================================================
    // JAUGENATUREL - Branches supplémentaires
    // ========================================================================

    /**
     * Test incrémentation puis décrémentation aux limites.
     */
    @Test
    public void testJaugeIncrementJusteAvantMax() {
        JaugeNaturel jauge = new JaugeNaturel(0, 3, 2);
        
        assertFalse("Pas encore au max", jauge.estRouge());
        
        jauge.incrementer();
        assertTrue("Maintenant au max", jauge.estRouge());
        assertEquals(3, jauge.getNiveau());
    }

    /**
     * Test décrémentation juste avant le min.
     */
    @Test
    public void testJaugeDecrementJusteAvantMin() {
        JaugeNaturel jauge = new JaugeNaturel(0, 3, 1);
        
        assertFalse("Pas encore au min", jauge.estVert());
        
        jauge.decrementer();
        assertTrue("Maintenant au min", jauge.estVert());
        assertEquals(0, jauge.getNiveau());
    }

    // ========================================================================
    // ETATPASSAGER - Transitions supplémentaires
    // ========================================================================

    /**
     * Test transition assis vers debout directe.
     */
    @Test
    public void testTransitionAssisVersDebout() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.ASSIS);
        IEtatPassager deboutEtat = etat.changerEnDebout();
        
        assertTrue(deboutEtat.estDebout());
        assertFalse(deboutEtat.estAssis());
        assertTrue(deboutEtat.estInterieur());
    }

    /**
     * Test transition debout vers assis directe.
     */
    @Test
    public void testTransitionDeboutVersAssis() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEBOUT);
        IEtatPassager assisEtat = etat.changerEnAssis();
        
        assertTrue(assisEtat.estAssis());
        assertFalse(assisEtat.estDebout());
        assertTrue(assisEtat.estInterieur());
    }

    /**
     * Test estInterieur retourne false pour DEHORS.
     */
    @Test
    public void testEstInterieurDehors() {
        EtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        assertFalse(etat.estInterieur());
        assertTrue(etat.estExterieur());
    }

    // ========================================================================
    // PASSAGERSTANDARD - Tests intégration complets
    // ========================================================================

    /**
     * Test passager destination 0 descend immédiatement à l'arrêt 0.
     * Note: Un passager ne peut pas descendre avant de monter.
     */
    @Test
    public void testPassagerDestinationZeroNeDescendPasAZero() throws UsagerInvalideException {
        PassagerStandard p = new PassagerStandard("Urgent", 0);
        Bus mockBus = mock(Bus.class);
        
        // Passager dehors à l'arrêt 0 = destination
        p.nouvelArret(mockBus, 0);
        
        // Assert - Passager dehors donc ne demande pas à sortir
        verify(mockBus, never()).demanderSortie(any());
    }

    /**
     * Test accepterPlaceAssise depuis état debout lance exception.
     */
    @Test(expected = IllegalStateException.class)
    public void testAccepterPlaceAssiseDepuisDebout() {
        PassagerStandard p = new PassagerStandard("Test", 5, new EtatPassager(EtatPassager.Etat.DEBOUT));
        p.accepterPlaceAssise(); // Pas dehors -> exception
    }

    /**
     * Test accepterPlaceDebout depuis état assis lance exception.
     */
    @Test(expected = IllegalStateException.class)
    public void testAccepterPlaceDeboutDepuisAssis() {
        PassagerStandard p = new PassagerStandard("Test", 5, new EtatPassager(EtatPassager.Etat.ASSIS));
        p.accepterPlaceDebout(); // Pas dehors -> exception
    }

    // ========================================================================
    // AUTOBUS - Remplissage du tableau passagers
    // ========================================================================

    /**
     * Test ajout de passager remplit le tableau correctement.
     */
    @Test
    public void testAjoutPassagerTableau() {
        Autobus busPetit = new Autobus(2, 2);
        
        Passager p1 = mock(Passager.class);
        when(p1.estDehors()).thenReturn(true);
        Passager p2 = mock(Passager.class);
        when(p2.estDehors()).thenReturn(true);
        Passager p3 = mock(Passager.class);
        when(p3.estDehors()).thenReturn(true);
        
        busPetit.demanderPlaceAssise(p1);
        busPetit.demanderPlaceAssise(p2);
        busPetit.demanderPlaceDebout(p3);
        
        assertEquals(2, busPetit.getNbAssis());
        assertEquals(1, busPetit.getNbDebout());
    }

    /**
     * Test retrait de passager libère la place dans le tableau.
     */
    @Test
    public void testRetraitPassagerTableau() {
        Autobus busPetit = new Autobus(1, 1);
        
        Passager p1 = mock(Passager.class);
        when(p1.estDehors()).thenReturn(true);
        busPetit.demanderPlaceAssise(p1);
        
        reset(p1);
        when(p1.estAssis()).thenReturn(true);
        when(p1.estDebout()).thenReturn(false);
        
        busPetit.demanderSortie(p1);
        assertEquals(0, busPetit.getNbAssis());
        
        // Nouveau passager peut prendre la place
        Passager p2 = mock(Passager.class);
        when(p2.estDehors()).thenReturn(true);
        busPetit.demanderPlaceAssise(p2);
        assertEquals(1, busPetit.getNbAssis());
    }

    /**
     * Test allerArretSuivant avec tableau contenant des null.
     */
    @Test
    public void testAllerArretSuivantTableauAvecTrous() throws UsagerInvalideException {
        Autobus busPetit = new Autobus(3, 0);
        
        Passager p1 = mock(Passager.class);
        Passager p2 = mock(Passager.class);
        when(p1.estDehors()).thenReturn(true);
        when(p2.estDehors()).thenReturn(true);
        
        busPetit.demanderPlaceAssise(p1);
        busPetit.demanderPlaceAssise(p2);
        
        // Retirer p1
        reset(p1);
        when(p1.estAssis()).thenReturn(true);
        when(p1.estDebout()).thenReturn(false);
        busPetit.demanderSortie(p1);
        
        // Maintenant tableau = [null, p2, null]
        // allerArretSuivant doit gérer les null
        busPetit.allerArretSuivant();
        
        // Assert - Seul p2 doit être notifié
        verify(p2).nouvelArret(busPetit, 1);
    }

    // ========================================================================
    // USAGERINVALIDEEXCEPTION - Couverture
    // ========================================================================

    @Test
    public void testUsagerInvalideExceptionConstructeurMessage() {
        UsagerInvalideException e = new UsagerInvalideException("Test message");
        assertEquals("Test message", e.getMessage());
    }

    @Test
    public void testUsagerInvalideExceptionConstructeurComplet() {
        PassagerStandard p = new PassagerStandard("Test", 5);
        Autobus bus = new Autobus(2, 2);
        
        UsagerInvalideException e = new UsagerInvalideException("Message", p, bus);
        
        assertEquals("Message", e.getMessage());
        assertEquals(p, e.quelUsager);
        assertEquals(bus, e.quelTransport);
    }

    // ========================================================================
    // TRANSPORTFACTORY - Couverture complète
    // ========================================================================

    @Test
    public void testTransportFactoryCreerAutobus() {
        Transport t = TransportFactory.creerAutobus(5, 10);
        assertNotNull(t);
        assertTrue(t instanceof Autobus);
        
        Autobus bus = (Autobus) t;
        assertTrue(bus.aPlaceAssise());
        assertTrue(bus.aPlaceDebout());
    }

    @Test
    public void testTransportFactoryCreerPassagerAvecNom() {
        Usager u = TransportFactory.creerPassager("MonNom", 7);
        
        assertNotNull(u);
        assertEquals("MonNom", u.nom());
        assertTrue(u.estDehors());
    }

    @Test
    public void testTransportFactoryCreerPassagerSansNom() {
        Usager u = TransportFactory.creerPassager(42);
        
        assertNotNull(u);
        assertEquals("PassagerStandard42", u.nom());
        assertTrue(u.estDehors());
    }

    @Test
    public void testTransportFactoryCreerAutobusUnParam() {
        Transport t = TransportFactory.creerAutobus(3);
        assertNotNull(t);
        assertTrue(t instanceof Autobus);
        
        Autobus bus = (Autobus) t;
        assertTrue(bus.aPlaceAssise());
        assertTrue(bus.aPlaceDebout());
    }

    @Test
    public void testUsagerInvalideExceptionGetMessage() {
        UsagerInvalideException e = new UsagerInvalideException("Mon message");
        assertEquals("Mon message", e.getMessage());
    }

    // ========================================================================
    // INTERFACES - Vérification via implémentations
    // ========================================================================

    @Test
    public void testIJaugeImplementation() {
        IJauge jauge = new JaugeNaturel(0, 10, 5);
        
        assertFalse(jauge.estVert());
        assertFalse(jauge.estRouge());
        assertEquals(5, jauge.getNiveau());
        
        jauge.incrementer();
        assertEquals(6, jauge.getNiveau());
        
        jauge.decrementer();
        assertEquals(5, jauge.getNiveau());
    }

    @Test
    public void testIEtatPassagerImplementation() {
        IEtatPassager etat = new EtatPassager(EtatPassager.Etat.DEHORS);
        
        assertTrue(etat.estExterieur());
        assertFalse(etat.estInterieur());
        assertFalse(etat.estAssis());
        assertFalse(etat.estDebout());
        
        IEtatPassager assis = etat.changerEnAssis();
        assertTrue(assis.estAssis());
        
        IEtatPassager debout = etat.changerEnDebout();
        assertTrue(debout.estDebout());
        
        IEtatPassager dehors = assis.changerEnDehors();
        assertTrue(dehors.estExterieur());
    }

    // ========================================================================
    // TESTS PASSAGERSTANDARD - Couverture complete monterDans
    // ========================================================================

    /**
     * Test monterDans quand bus a places assises et debout - priorite assis.
     * Couvre la branche bus.aPlaceAssise() == true dans monterDans.
     */
    @Test
    public void testMonterDansPrioritePlaceAssise() throws UsagerInvalideException {
        Autobus bus = new Autobus(2, 2);
        PassagerStandard p = new PassagerStandard("Test", 10);
        
        p.monterDans(bus);
        
        assertTrue("Le passager devrait être assis (priorité)", p.estAssis());
        assertEquals(1, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
    }

    /**
     * Test monterDans quand bus n'a que places debout.
     * Couvre la branche else if (bus.aPlaceDebout()) dans monterDans.
     */
    @Test
    public void testMonterDansSeulementsPlaceDebout() throws UsagerInvalideException {
        Autobus bus = new Autobus(0, 2);
        PassagerStandard p = new PassagerStandard("Test", 10);
        
        p.monterDans(bus);
        
        assertTrue("Le passager devrait être debout", p.estDebout());
        assertEquals(0, bus.getNbAssis());
        assertEquals(1, bus.getNbDebout());
    }

    // ========================================================================
    // TESTS AUTOBUS - Couverture demanderChangerEnDebout/Assis avec bonnes conditions
    // ========================================================================

    /**
     * Test changement debout vers assis avec vrai passager.
     */
    @Test
    public void testChangementDeboutVersAssisIntegration() {
        Autobus bus = new Autobus(2, 2);
        PassagerStandard p = new PassagerStandard("Test", 10);
        
        bus.demanderPlaceDebout(p);
        assertTrue(p.estDebout());
        
        bus.demanderChangerEnAssis(p);
        assertTrue(p.estAssis());
    }

    /**
     * Test changement assis vers debout avec vrai passager.
     */
    @Test
    public void testChangementAssisVersDeboutIntegration() {
        Autobus bus = new Autobus(2, 2);
        PassagerStandard p = new PassagerStandard("Test", 10);
        
        bus.demanderPlaceAssise(p);
        assertTrue(p.estAssis());
        
        bus.demanderChangerEnDebout(p);
        assertTrue(p.estDebout());
    }

    // ========================================================================
    // TESTS POUR BRANCHES DIFFICILES A ATTEINDRE
    // ========================================================================

    /**
     * Test ajouterPassager avec tableau complètement rempli puis vidé.
     * Vérifie que le tableau est bien géré avec des entrées/sorties.
     */
    @Test
    public void testAjoutRetraitMultiplePassagers() {
        Autobus busTiny = new Autobus(2, 0);
        
        Passager p1 = mock(Passager.class);
        Passager p2 = mock(Passager.class);
        
        when(p1.estDehors()).thenReturn(true);
        when(p2.estDehors()).thenReturn(true);
        
        busTiny.demanderPlaceAssise(p1);
        busTiny.demanderPlaceAssise(p2);
        
        assertEquals(2, busTiny.getNbAssis());
        assertFalse(busTiny.aPlaceAssise());
        
        // Retrait du premier
        reset(p1);
        when(p1.estAssis()).thenReturn(true);
        busTiny.demanderSortie(p1);
        
        assertTrue(busTiny.aPlaceAssise());
        
        // Ajout d'un nouveau
        Passager p3 = mock(Passager.class);
        when(p3.estDehors()).thenReturn(true);
        busTiny.demanderPlaceAssise(p3);
        
        assertEquals(2, busTiny.getNbAssis());
    }

    /**
     * Test monterDans quand seulement places debout disponibles.
     * Couvre bien la branche else if (bus.aPlaceDebout()).
     */
    @Test
    public void testMonterDansSeulementsDeboutMockTransport() throws UsagerInvalideException {
        Transport mockTransport = mock(Transport.class, withSettings().extraInterfaces(Bus.class));
        Bus busFromTransport = (Bus) mockTransport;
        
        // Le bus n'a PAS de places assises mais a des places debout
        when(busFromTransport.aPlaceAssise()).thenReturn(false);
        when(busFromTransport.aPlaceDebout()).thenReturn(true);
        
        doAnswer(invocation -> {
            Passager p = invocation.getArgument(0);
            p.accepterPlaceDebout();
            return null;
        }).when(busFromTransport).demanderPlaceDebout(any(Passager.class));
        
        PassagerStandard p = new PassagerStandard("Test", 10);
        p.monterDans(mockTransport);
        
        // Assert - La branche else if est bien prise
        verify(busFromTransport, never()).demanderPlaceAssise(any());
        verify(busFromTransport).demanderPlaceDebout(p);
        assertTrue(p.estDebout());
    }

    /**
     * Test allerArretSuivant avec bus vide.
     * Couvre la boucle for quand tous les éléments sont null.
     */
    @Test
    public void testAllerArretSuivantBusVide() throws UsagerInvalideException {
        Autobus busVide = new Autobus(3, 3);
        
        // Aucun passager dans le bus
        assertEquals(0, busVide.getNbAssis());
        assertEquals(0, busVide.getNbDebout());
        
        // Avancer sans passager
        busVide.allerArretSuivant();
        
        assertEquals(1, busVide.getNumeroArret());
    }

    /**
     * Test ajouterPassager quand le tableau a des trous.
     */
    @Test
    public void testAjouterPassagerTableauAvecTrous() {
        Autobus busPetit = new Autobus(3, 0);
        
        Passager p1 = mock(Passager.class);
        Passager p2 = mock(Passager.class);
        Passager p3 = mock(Passager.class);
        
        when(p1.estDehors()).thenReturn(true);
        when(p2.estDehors()).thenReturn(true);
        when(p3.estDehors()).thenReturn(true);
        
        // Ajouter 3 passagers
        busPetit.demanderPlaceAssise(p1);
        busPetit.demanderPlaceAssise(p2);
        busPetit.demanderPlaceAssise(p3);
        assertEquals(3, busPetit.getNbAssis());
        
        // Retirer le passager du milieu
        reset(p2);
        when(p2.estAssis()).thenReturn(true);
        busPetit.demanderSortie(p2);
        assertEquals(2, busPetit.getNbAssis());
        
        // Ajouter un nouveau - doit trouver la place libre au milieu
        Passager p4 = mock(Passager.class);
        when(p4.estDehors()).thenReturn(true);
        busPetit.demanderPlaceAssise(p4);
        assertEquals(3, busPetit.getNbAssis());
    }
}
