package tec;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests unitaires pour PassagerStandard utilisant Mockito.
 * 
 * Cette classe remplace les FauxBus* par des mocks Mockito
 * pour tester le comportement de PassagerStandard.
 * 
 * Structure des tests :
 * - Arrange : Configuration des mocks avec when().thenReturn()
 * - Act : Exécution de la méthode testée
 * - Assert : Vérification avec verify() et assertions JUnit
 */
public class PassagerStandardTestMockito {

    private PassagerStandard passager;

    @Before
    public void setUp() {
        // Passager par défaut pour la plupart des tests
        passager = new PassagerStandard("Tom", 6, new EtatPassager(EtatPassager.Etat.DEHORS));
    }

    // ========================================================================
    // TESTS DES ÉTATS DE BASE
    // ========================================================================

    /**
     * Teste les états de base d'un passager : assis, debout, dehors.
     */
    @Test
    public void testEtatsDeBase() {
        assertTrue("Le passager devrait être dehors", passager.estDehors());
        assertFalse("Le passager ne devrait pas être assis", passager.estAssis());
        assertFalse("Le passager ne devrait pas être debout", passager.estDebout());
    }

    @Test
    public void testConstructeurAvecDestinationSeule() {
        PassagerStandard p = new PassagerStandard(5);
        
        assertEquals("PassagerStandard5", p.nom());
        assertTrue(p.estDehors());
        assertEquals(5, p.getDestination());
    }

    @Test
    public void testInstanciationAssis() {
        PassagerStandard p = new PassagerStandard("Alice", 3, new EtatPassager(EtatPassager.Etat.ASSIS));
        
        assertFalse("Ne devrait pas être dehors", p.estDehors());
        assertTrue("Devrait être assis", p.estAssis());
        assertFalse("Ne devrait pas être debout", p.estDebout());
    }

    @Test
    public void testInstanciationDebout() {
        PassagerStandard p = new PassagerStandard("Bob", 4, new EtatPassager(EtatPassager.Etat.DEBOUT));
        
        assertFalse("Ne devrait pas être dehors", p.estDehors());
        assertFalse("Ne devrait pas être assis", p.estAssis());
        assertTrue("Devrait être debout", p.estDebout());
    }

    @Test
    public void testInstanciationDehors() {
        PassagerStandard p = new PassagerStandard("Charlie", 5, new EtatPassager(EtatPassager.Etat.DEHORS));
        
        assertTrue("Devrait être dehors", p.estDehors());
        assertFalse("Ne devrait pas être assis", p.estAssis());
        assertFalse("Ne devrait pas être debout", p.estDebout());
    }

    // ========================================================================
    // TESTS DES CHANGEMENTS D'ÉTAT (accepter*)
    // ========================================================================

    @Test
    public void testAccepterPlaceAssise() {
        passager.accepterPlaceAssise();
        
        assertTrue("Le passager devrait être assis", passager.estAssis());
        assertFalse("Le passager ne devrait pas être debout", passager.estDebout());
        assertFalse("Le passager ne devrait pas être dehors", passager.estDehors());
    }

    @Test
    public void testAccepterPlaceDebout() {
        passager.accepterPlaceDebout();
        
        assertFalse("Le passager ne devrait pas être assis", passager.estAssis());
        assertTrue("Le passager devrait être debout", passager.estDebout());
        assertFalse("Le passager ne devrait pas être dehors", passager.estDehors());
    }

    @Test
    public void testAccepterSortieDepuisAssis() {
        passager.accepterPlaceAssise();
        passager.accepterSortie();
        
        assertFalse("Le passager ne devrait pas être assis", passager.estAssis());
        assertFalse("Le passager ne devrait pas être debout", passager.estDebout());
        assertTrue("Le passager devrait être dehors", passager.estDehors());
    }

    @Test
    public void testAccepterSortieDepuisDebout() {
        passager.accepterPlaceDebout();
        passager.accepterSortie();
        
        assertFalse("Le passager ne devrait pas être assis", passager.estAssis());
        assertFalse("Le passager ne devrait pas être debout", passager.estDebout());
        assertTrue("Le passager devrait être dehors", passager.estDehors());
    }

    // ========================================================================
    // TESTS AVEC MOCK BUS - REMPLACEMENT DES FAUX BUS
    // ========================================================================

    /**
     * Test équivalent à FauxBusAssis : le bus a une place assise.
     * Vérifie que monterDans() demande une place assise.
     */
    @Test
    public void testMonterDansBusAvecPlaceAssise() throws UsagerInvalideException {
        // Arrange - Mock équivalent à FauxBusAssis
        Bus mockBus = mock(Bus.class);
        Transport mockTransport = mock(Transport.class, withSettings().extraInterfaces(Bus.class));
        Bus busFromTransport = (Bus) mockTransport;
        
        when(busFromTransport.aPlaceAssise()).thenReturn(true);
        when(busFromTransport.aPlaceDebout()).thenReturn(false);
        
        // Simuler le comportement du bus qui accepte le passager
        doAnswer(invocation -> {
            Passager p = invocation.getArgument(0);
            p.accepterPlaceAssise();
            return null;
        }).when(busFromTransport).demanderPlaceAssise(any(Passager.class));

        // Act
        passager.monterDans(mockTransport);

        // Assert
        verify(busFromTransport).demanderPlaceAssise(passager);
        verify(busFromTransport, never()).demanderPlaceDebout(any());
        assertTrue("Le passager devrait être assis", passager.estAssis());
    }

    /**
     * Test équivalent à FauxBusDebout : le bus n'a que des places debout.
     * Vérifie que monterDans() demande une place debout.
     */
    @Test
    public void testMonterDansBusAvecPlaceDeboutSeulement() throws UsagerInvalideException {
        // Arrange - Mock équivalent à FauxBusDebout
        Transport mockTransport = mock(Transport.class, withSettings().extraInterfaces(Bus.class));
        Bus busFromTransport = (Bus) mockTransport;
        
        when(busFromTransport.aPlaceAssise()).thenReturn(false);
        when(busFromTransport.aPlaceDebout()).thenReturn(true);
        
        doAnswer(invocation -> {
            Passager p = invocation.getArgument(0);
            p.accepterPlaceDebout();
            return null;
        }).when(busFromTransport).demanderPlaceDebout(any(Passager.class));

        // Act
        passager.monterDans(mockTransport);

        // Assert
        verify(busFromTransport, never()).demanderPlaceAssise(any());
        verify(busFromTransport).demanderPlaceDebout(passager);
        assertTrue("Le passager devrait être debout", passager.estDebout());
    }

    /**
     * Test équivalent à FauxBusPlein : le bus n'a aucune place.
     * Vérifie qu'une UsagerInvalideException est levée.
     */
    @Test
    public void testMonterDansBusPlein() {
        // Arrange - Mock équivalent à FauxBusPlein
        Transport mockTransport = mock(Transport.class, withSettings().extraInterfaces(Bus.class));
        Bus busFromTransport = (Bus) mockTransport;
        
        when(busFromTransport.aPlaceAssise()).thenReturn(false);
        when(busFromTransport.aPlaceDebout()).thenReturn(false);

        // Act & Assert
        try {
            passager.monterDans(mockTransport);
            fail("UsagerInvalideException devait être levée");
        } catch (UsagerInvalideException e) {
            assertEquals("Aucune place disponible", e.getMessage());
            assertEquals(passager, e.quelUsager);
            assertEquals(mockTransport, e.quelTransport);
        }

        // Vérifier que le passager est resté dehors
        assertTrue("Le passager devrait rester dehors", passager.estDehors());
        
        // Vérifier qu'aucune demande de place n'a été faite
        verify(busFromTransport, never()).demanderPlaceAssise(any());
        verify(busFromTransport, never()).demanderPlaceDebout(any());
    }

    /**
     * Test équivalent à FauxBusVide : le bus a des places assises et debout.
     * Priorité aux places assises.
     */
    @Test
    public void testMonterDansBusVide() throws UsagerInvalideException {
        // Arrange - Mock équivalent à FauxBusVide
        Transport mockTransport = mock(Transport.class, withSettings().extraInterfaces(Bus.class));
        Bus busFromTransport = (Bus) mockTransport;
        
        when(busFromTransport.aPlaceAssise()).thenReturn(true);
        when(busFromTransport.aPlaceDebout()).thenReturn(true);
        
        doAnswer(invocation -> {
            Passager p = invocation.getArgument(0);
            p.accepterPlaceAssise();
            return null;
        }).when(busFromTransport).demanderPlaceAssise(any(Passager.class));

        // Act
        passager.monterDans(mockTransport);

        // Assert - Priorité aux places assises
        verify(busFromTransport).demanderPlaceAssise(passager);
        verify(busFromTransport, never()).demanderPlaceDebout(any());
        assertTrue("Le passager devrait être assis", passager.estAssis());
    }

    // ========================================================================
    // TESTS DE NOUVEL ARRÊT AVEC MOCK BUS
    // ========================================================================

    /**
     * Test nouvelArret quand le passager n'est pas à destination.
     */
    @Test
    public void testNouvelArretPasADestination() {
        // Arrange
        Bus mockBus = mock(Bus.class);
        PassagerStandard p = new PassagerStandard("Test", 5, new EtatPassager(EtatPassager.Etat.ASSIS));

        // Act - Arrêt 3, destination 5
        p.nouvelArret(mockBus, 3);

        // Assert - Le passager ne demande pas à sortir
        verify(mockBus, never()).demanderSortie(any());
        assertTrue("Le passager devrait rester assis", p.estAssis());
    }

    /**
     * Test nouvelArret quand le passager arrive à destination.
     */
    @Test
    public void testNouvelArretADestination() {
        // Arrange
        Bus mockBus = mock(Bus.class);
        PassagerStandard p = new PassagerStandard("Test", 5, new EtatPassager(EtatPassager.Etat.ASSIS));
        
        doAnswer(invocation -> {
            Passager passager = invocation.getArgument(0);
            passager.accepterSortie();
            return null;
        }).when(mockBus).demanderSortie(any(Passager.class));

        // Act - Arrêt 5 = destination
        p.nouvelArret(mockBus, 5);

        // Assert - Le passager demande à sortir
        verify(mockBus).demanderSortie(p);
        assertTrue("Le passager devrait être dehors", p.estDehors());
    }

    /**
     * Test nouvelArret quand le passager est déjà dehors.
     */
    @Test
    public void testNouvelArretPassagerDehors() {
        // Arrange
        Bus mockBus = mock(Bus.class);
        // passager est dehors par défaut (destination 6)

        // Act
        passager.nouvelArret(mockBus, 6);

        // Assert - Le passager dehors ne demande pas à sortir
        verify(mockBus, never()).demanderSortie(any());
        assertTrue("Le passager devrait rester dehors", passager.estDehors());
    }

    // ========================================================================
    // TESTS DES EXCEPTIONS ET CAS LIMITES
    // ========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructeurNomNull() {
        new PassagerStandard(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructeurDestinationNegative() {
        new PassagerStandard("Test", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructeurEtatNull() {
        new PassagerStandard("Test", 5, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAccepterSortiePassagerDejaHors() {
        // passager est dehors par défaut
        passager.accepterSortie();
    }

    @Test(expected = IllegalStateException.class)
    public void testAccepterPlaceAssisePassagerPasDehors() {
        passager.accepterPlaceAssise(); // passe assis
        passager.accepterPlaceAssise(); // déjà assis -> exception
    }

    @Test(expected = IllegalStateException.class)
    public void testAccepterPlaceDeboutPassagerPasDehors() {
        passager.accepterPlaceDebout(); // passe debout
        passager.accepterPlaceDebout(); // déjà debout -> exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNouvelArretBusNull() {
        passager.nouvelArret(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNouvelArretNumeroNegatif() {
        Bus mockBus = mock(Bus.class);
        passager.nouvelArret(mockBus, -1);
    }

    @Test
    public void testMonterDansTransportNull() {
        try {
            passager.monterDans(null);
            fail("UsagerInvalideException attendue");
        } catch (UsagerInvalideException e) {
            assertEquals(passager, e.quelUsager);
            assertNull(e.quelTransport);
        }
    }

    // ========================================================================
    // TESTS DE VÉRIFICATION DES APPELS (verify)
    // ========================================================================

    @Test
    public void testVerificationAppelsMethodesBus() throws UsagerInvalideException {
        // Arrange
        Transport mockTransport = mock(Transport.class, withSettings().extraInterfaces(Bus.class));
        Bus busFromTransport = (Bus) mockTransport;
        
        when(busFromTransport.aPlaceAssise()).thenReturn(true);
        when(busFromTransport.aPlaceDebout()).thenReturn(true);
        
        doAnswer(invocation -> {
            Passager p = invocation.getArgument(0);
            p.accepterPlaceAssise();
            return null;
        }).when(busFromTransport).demanderPlaceAssise(any(Passager.class));

        // Act
        passager.monterDans(mockTransport);

        // Assert - Vérification de l'ordre des appels
        // aPlaceAssise() est appelé 2 fois: une pour la vérification initiale, une pour décider le type de place
        verify(busFromTransport, times(2)).aPlaceAssise();
        verify(busFromTransport).demanderPlaceAssise(passager);
    }

    @Test
    public void testNomPassager() {
        assertEquals("Tom", passager.nom());
    }

    @Test
    public void testToString() {
        String result = passager.toString();
        assertTrue("Le toString devrait contenir le nom", result.contains("Tom"));
        assertTrue("Le toString devrait contenir l'état", result.contains("dehors"));
    }

    @Test
    public void testGetDestination() {
        assertEquals(6, passager.getDestination());
    }
}
