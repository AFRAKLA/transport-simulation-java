package tec;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires pour Autobus utilisant Mockito.
 * 
 * Cette classe remplace FauxPassager par des mocks Mockito
 * pour tester le comportement de la classe Autobus.
 * 
 * Les mocks permettent de :
 * - Simuler différents états des passagers (assis, debout, dehors)
 * - Vérifier les appels aux méthodes accepter*()
 * - Tester les interactions entre Autobus et Passager
 */
public class AutobusTestMockito {

    private Autobus bus;

    @Before
    public void setUp() {
        // Bus avec 2 places assises et 3 places debout
        bus = new Autobus(2, 3);
    }

    // ========================================================================
    // TESTS DE CRÉATION
    // ========================================================================

    @Test
    public void testCreationAutobus() {
        assertTrue("Devrait avoir des places assises", bus.aPlaceAssise());
        assertTrue("Devrait avoir des places debout", bus.aPlaceDebout());
        assertEquals("[arret:0, assis:0, debout:0]", bus.toString());
    }

    @Test
    public void testCreationAutobusAvecUnSeulParametre() {
        Autobus busSimple = new Autobus(5);
        assertTrue(busSimple.aPlaceAssise());
        assertTrue(busSimple.aPlaceDebout());
    }

    @Test(expected = UsagerInvalideException.class)
    public void testCreationAutobusPlacesAssisesNegatives() {
        new Autobus(-1, 3);
    }

    @Test(expected = UsagerInvalideException.class)
    public void testCreationAutobusPlacesDeboutNegatives() {
        new Autobus(2, -1);
    }

    // ========================================================================
    // TESTS DEMANDER PLACE ASSISE AVEC MOCK PASSAGER
    // ========================================================================

    /**
     * Test de demanderPlaceAssise avec un mock Passager.
     * Vérifie que accepterPlaceAssise() est appelé.
     */
    @Test
    public void testDemanderPlaceAssiseAvecMock() {
        // Arrange
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        when(mockPassager.estAssis()).thenReturn(false);
        when(mockPassager.estDebout()).thenReturn(false);

        // Act
        bus.demanderPlaceAssise(mockPassager);

        // Assert
        verify(mockPassager).accepterPlaceAssise();
        assertEquals(1, bus.getNbAssis());
    }

    @Test
    public void testDemanderPlaceAssisePassagerNonDehors() {
        // Arrange - Passager déjà dans le bus (assis)
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(false);
        when(mockPassager.estAssis()).thenReturn(true);

        // Act
        bus.demanderPlaceAssise(mockPassager);

        // Assert - accepterPlaceAssise ne doit pas être appelé
        verify(mockPassager, never()).accepterPlaceAssise();
        assertEquals(0, bus.getNbAssis());
    }

    @Test
    public void testDemanderPlaceAssiseBusPlein() {
        // Arrange - Remplir toutes les places assises
        Autobus busPetit = new Autobus(1, 1);
        
        Passager mockPassager1 = mock(Passager.class);
        when(mockPassager1.estDehors()).thenReturn(true);
        busPetit.demanderPlaceAssise(mockPassager1);
        
        Passager mockPassager2 = mock(Passager.class);
        when(mockPassager2.estDehors()).thenReturn(true);

        // Act
        busPetit.demanderPlaceAssise(mockPassager2);

        // Assert - Pas de place, pas d'appel à accepterPlaceAssise
        verify(mockPassager2, never()).accepterPlaceAssise();
    }

    @Test(expected = UsagerInvalideException.class)
    public void testDemanderPlaceAssisePassagerNull() {
        bus.demanderPlaceAssise(null);
    }

    // ========================================================================
    // TESTS DEMANDER PLACE DEBOUT AVEC MOCK PASSAGER
    // ========================================================================

    @Test
    public void testDemanderPlaceDeboutAvecMock() {
        // Arrange
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);

        // Act
        bus.demanderPlaceDebout(mockPassager);

        // Assert
        verify(mockPassager).accepterPlaceDebout();
        assertEquals(1, bus.getNbDebout());
    }

    @Test
    public void testDemanderPlaceDeboutPassagerNonDehors() {
        // Arrange
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(false);

        // Act
        bus.demanderPlaceDebout(mockPassager);

        // Assert
        verify(mockPassager, never()).accepterPlaceDebout();
        assertEquals(0, bus.getNbDebout());
    }

    @Test(expected = UsagerInvalideException.class)
    public void testDemanderPlaceDeboutPassagerNull() {
        bus.demanderPlaceDebout(null);
    }

    // ========================================================================
    // TESTS DEMANDER SORTIE AVEC MOCK PASSAGER
    // ========================================================================

    @Test
    public void testDemanderSortiePassagerAssis() {
        // Arrange - Ajouter un passager assis
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceAssise(mockPassager);
        
        // Reset et reconfigurer le mock
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(true);
        when(mockPassager.estDebout()).thenReturn(false);

        // Act
        bus.demanderSortie(mockPassager);

        // Assert
        verify(mockPassager).accepterSortie();
        assertEquals(0, bus.getNbAssis());
    }

    @Test
    public void testDemanderSortiePassagerDebout() {
        // Arrange
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceDebout(mockPassager);
        
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(false);
        when(mockPassager.estDebout()).thenReturn(true);

        // Act
        bus.demanderSortie(mockPassager);

        // Assert
        verify(mockPassager).accepterSortie();
        assertEquals(0, bus.getNbDebout());
    }

    @Test(expected = UsagerInvalideException.class)
    public void testDemanderSortiePassagerNull() {
        bus.demanderSortie(null);
    }

    // ========================================================================
    // TESTS CHANGEMENT DE PLACE AVEC MOCK PASSAGER
    // ========================================================================

    @Test
    public void testDemanderChangerEnDebout() {
        // Arrange - Passager assis qui veut passer debout
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estAssis()).thenReturn(true);
        when(mockPassager.estDebout()).thenReturn(false);

        // Simuler ajout préalable
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceAssise(mockPassager);
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(true);

        // Act
        bus.demanderChangerEnDebout(mockPassager);

        // Assert
        verify(mockPassager).accepterSortie();
        verify(mockPassager).accepterPlaceDebout();
    }

    @Test
    public void testDemanderChangerEnDeboutSansPlaceDebout() {
        // Arrange - Bus sans places debout
        Autobus busSpecial = new Autobus(2, 0);
        
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        busSpecial.demanderPlaceAssise(mockPassager);
        
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(true);

        // Act
        busSpecial.demanderChangerEnDebout(mockPassager);

        // Assert - Pas de changement car pas de place debout
        verify(mockPassager, never()).accepterSortie();
        verify(mockPassager, never()).accepterPlaceDebout();
    }

    @Test
    public void testDemanderChangerEnAssis() {
        // Arrange - Passager debout qui veut passer assis
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceDebout(mockPassager);
        
        reset(mockPassager);
        when(mockPassager.estDebout()).thenReturn(true);
        when(mockPassager.estAssis()).thenReturn(false);

        // Act
        bus.demanderChangerEnAssis(mockPassager);

        // Assert
        verify(mockPassager).accepterSortie();
        verify(mockPassager).accepterPlaceAssise();
    }

    @Test
    public void testDemanderChangerEnAssisSansPlaceAssise() {
        // Arrange - Bus sans places assises
        Autobus busSpecial = new Autobus(0, 2);
        
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        busSpecial.demanderPlaceDebout(mockPassager);
        
        reset(mockPassager);
        when(mockPassager.estDebout()).thenReturn(true);

        // Act
        busSpecial.demanderChangerEnAssis(mockPassager);

        // Assert - Pas de changement car pas de place assise
        verify(mockPassager, never()).accepterSortie();
        verify(mockPassager, never()).accepterPlaceAssise();
    }

    // ========================================================================
    // TESTS ALLER ARRÊT SUIVANT
    // ========================================================================

    @Test
    public void testAllerArretSuivant() throws UsagerInvalideException {
        bus.allerArretSuivant();
        assertEquals(1, bus.getNumeroArret());
        assertEquals("[arret:1, assis:0, debout:0]", bus.toString());
    }

    @Test
    public void testAllerArretSuivantAvecPassagers() throws UsagerInvalideException {
        // Arrange
        Passager mockPassager1 = mock(Passager.class);
        Passager mockPassager2 = mock(Passager.class);
        
        when(mockPassager1.estDehors()).thenReturn(true);
        when(mockPassager2.estDehors()).thenReturn(true);
        
        bus.demanderPlaceAssise(mockPassager1);
        bus.demanderPlaceDebout(mockPassager2);

        // Act
        bus.allerArretSuivant();

        // Assert - Tous les passagers sont notifiés
        verify(mockPassager1).nouvelArret(bus, 1);
        verify(mockPassager2).nouvelArret(bus, 1);
    }

    @Test
    public void testAllerArretSuivantPassagerDescend() throws UsagerInvalideException {
        // Arrange - Passager qui descend à l'arrêt 1
        Passager mockPassager = mock(Passager.class);
        when(mockPassager.estDehors()).thenReturn(true);
        bus.demanderPlaceAssise(mockPassager);
        
        // Simuler que le passager demande à sortir lors de nouvelArret
        doAnswer(invocation -> {
            bus.demanderSortie(mockPassager);
            return null;
        }).when(mockPassager).nouvelArret(eq(bus), eq(1));
        
        reset(mockPassager);
        when(mockPassager.estAssis()).thenReturn(true);
        when(mockPassager.estDebout()).thenReturn(false);
        when(mockPassager.estDehors()).thenReturn(false);
        
        doAnswer(invocation -> {
            bus.demanderSortie(mockPassager);
            return null;
        }).when(mockPassager).nouvelArret(eq(bus), eq(1));

        // Act
        bus.allerArretSuivant();

        // Assert
        verify(mockPassager).nouvelArret(bus, 1);
    }

    // ========================================================================
    // TESTS DE CAPACITÉ
    // ========================================================================

    @Test
    public void testBusPlein() {
        Autobus busTresPetit = new Autobus(0, 0);
        
        assertFalse("Pas de places assises", busTresPetit.aPlaceAssise());
        assertFalse("Pas de places debout", busTresPetit.aPlaceDebout());
    }

    @Test
    public void testRemplissageProgressif() {
        Autobus busPetit = new Autobus(2, 2);
        
        // Remplir les places assises
        for (int i = 0; i < 2; i++) {
            assertTrue("Devrait avoir des places assises", busPetit.aPlaceAssise());
            Passager mock = mock(Passager.class);
            when(mock.estDehors()).thenReturn(true);
            busPetit.demanderPlaceAssise(mock);
        }
        
        assertFalse("Plus de places assises", busPetit.aPlaceAssise());
        assertTrue("Encore des places debout", busPetit.aPlaceDebout());
        
        // Remplir les places debout
        for (int i = 0; i < 2; i++) {
            assertTrue("Devrait avoir des places debout", busPetit.aPlaceDebout());
            Passager mock = mock(Passager.class);
            when(mock.estDehors()).thenReturn(true);
            busPetit.demanderPlaceDebout(mock);
        }
        
        assertFalse("Plus de places debout", busPetit.aPlaceDebout());
    }

    // ========================================================================
    // TESTS AVEC VRAIS PASSAGERS (intégration)
    // ========================================================================

    @Test
    public void testIntegrationAvecPassagerStandard() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Test", 2);
        
        bus.demanderPlaceAssise(passager);
        
        assertTrue("Le passager devrait être assis", passager.estAssis());
        assertEquals(1, bus.getNbAssis());
    }

    @Test
    public void testIntegrationPassagerDescendADestination() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Test", 1);
        
        bus.demanderPlaceAssise(passager);
        assertTrue(passager.estAssis());
        
        bus.allerArretSuivant(); // Arrêt 1 = destination
        
        assertTrue("Le passager devrait être dehors", passager.estDehors());
        assertEquals(0, bus.getNbAssis());
    }

    @Test
    public void testIntegrationChangementPlace() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Test", 5);
        
        bus.demanderPlaceAssise(passager);
        assertTrue(passager.estAssis());
        assertEquals(1, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
        
        bus.demanderChangerEnDebout(passager);
        assertTrue(passager.estDebout());
        assertEquals(0, bus.getNbAssis());
        assertEquals(1, bus.getNbDebout());
    }

    // ========================================================================
    // TESTS SUPPLÉMENTAIRES POUR LA COUVERTURE
    // ========================================================================

    @Test
    public void testToString() {
        assertEquals("[arret:0, assis:0, debout:0]", bus.toString());
        
        Passager mock = mock(Passager.class);
        when(mock.estDehors()).thenReturn(true);
        bus.demanderPlaceAssise(mock);
        
        assertEquals("[arret:0, assis:1, debout:0]", bus.toString());
    }

    @Test
    public void testGetters() {
        assertEquals(0, bus.getNumeroArret());
        assertEquals(0, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
    }
}
