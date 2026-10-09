package tec;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests fonctionnels et d'intégration pour le système de transport.
 * 
 * Ces tests vérifient des scénarios complets impliquant plusieurs
 * composants du système (Autobus, PassagerStandard, etc.)
 */
public class TestsFonctionnels {

    private Autobus bus;

    @Before
    public void setUp() {
        bus = new Autobus(3, 5); // 3 assis, 5 debout
    }

    // ========================================================================
    // SCÉNARIO 1 : Cycle de vie complet d'un passager
    // ========================================================================

    @Test
    public void testCycleVieCompletPassager() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Alice", 3);
        
        // État initial : dehors
        assertTrue("Devrait être dehors", passager.estDehors());
        
        // Monte dans le bus
        passager.monterDans(bus);
        assertTrue("Devrait être assis", passager.estAssis());
        
        // Avance jusqu'à destination
        bus.allerArretSuivant(); // Arrêt 1
        assertTrue("Devrait encore être assis", passager.estAssis());
        
        bus.allerArretSuivant(); // Arrêt 2
        assertTrue("Devrait encore être assis", passager.estAssis());
        
        bus.allerArretSuivant(); // Arrêt 3 = destination
        assertTrue("Devrait être descendu", passager.estDehors());
    }

    // ========================================================================
    // SCÉNARIO 2 : Plusieurs passagers avec différentes destinations
    // ========================================================================

    @Test
    public void testPlusieursPassagersDifferentesDestinations() throws UsagerInvalideException {
        PassagerStandard p1 = new PassagerStandard("P1", 1);
        PassagerStandard p2 = new PassagerStandard("P2", 2);
        PassagerStandard p3 = new PassagerStandard("P3", 3);
        
        p1.monterDans(bus);
        p2.monterDans(bus);
        p3.monterDans(bus);
        
        assertEquals("3 passagers assis", 3, bus.getNbAssis());
        
        bus.allerArretSuivant(); // Arrêt 1 - P1 descend
        assertTrue("P1 devrait être dehors", p1.estDehors());
        assertFalse("P2 devrait être dans le bus", p2.estDehors());
        assertFalse("P3 devrait être dans le bus", p3.estDehors());
        assertEquals("2 passagers assis", 2, bus.getNbAssis());
        
        bus.allerArretSuivant(); // Arrêt 2 - P2 descend
        assertTrue("P2 devrait être dehors", p2.estDehors());
        assertFalse("P3 devrait être dans le bus", p3.estDehors());
        assertEquals("1 passager assis", 1, bus.getNbAssis());
        
        bus.allerArretSuivant(); // Arrêt 3 - P3 descend
        assertTrue("P3 devrait être dehors", p3.estDehors());
        assertEquals("Bus vide", 0, bus.getNbAssis());
    }

    // ========================================================================
    // SCÉNARIO 3 : Remplissage complet du bus
    // ========================================================================

    @Test
    public void testRemplissageComplet() throws UsagerInvalideException {
        Autobus busPetit = new Autobus(2, 2); // 4 places total
        
        PassagerStandard[] passagers = new PassagerStandard[4];
        for (int i = 0; i < 4; i++) {
            passagers[i] = new PassagerStandard("P" + i, 10);
            passagers[i].monterDans(busPetit);
        }
        
        // 2 assis + 2 debout
        assertEquals(2, busPetit.getNbAssis());
        assertEquals(2, busPetit.getNbDebout());
        
        // Vérifier états
        assertTrue("P0 assis", passagers[0].estAssis());
        assertTrue("P1 assis", passagers[1].estAssis());
        assertTrue("P2 debout", passagers[2].estDebout());
        assertTrue("P3 debout", passagers[3].estDebout());
        
        // Tentative de montée 5ème passager
        PassagerStandard p5 = new PassagerStandard("P5", 10);
        try {
            p5.monterDans(busPetit);
            fail("Exception attendue pour bus plein");
        } catch (UsagerInvalideException e) {
            assertTrue("P5 devrait être dehors", p5.estDehors());
        }
    }

    // ========================================================================
    // SCÉNARIO 4 : Changements de places
    // ========================================================================

    @Test
    public void testChangementPlaceAssisVersDebout() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Test", 10);
        
        bus.demanderPlaceAssise(passager);
        assertTrue(passager.estAssis());
        assertEquals(1, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
        
        bus.demanderChangerEnDebout(passager);
        assertTrue(passager.estDebout());
        assertEquals(0, bus.getNbAssis());
        assertEquals(1, bus.getNbDebout());
    }

    @Test
    public void testChangementPlaceDeboutVersAssis() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Test", 10);
        
        bus.demanderPlaceDebout(passager);
        assertTrue(passager.estDebout());
        
        bus.demanderChangerEnAssis(passager);
        assertTrue(passager.estAssis());
        assertEquals(1, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
    }

    // ========================================================================
    // SCÉNARIO 5 : Montées et descentes multiples au même arrêt
    // ========================================================================

    @Test
    public void testFluxPassagersAUnArret() throws UsagerInvalideException {
        // 3 passagers initiaux
        PassagerStandard p1 = new PassagerStandard("DescendArret2", 2);
        PassagerStandard p2 = new PassagerStandard("DescendArret2aussi", 2);
        PassagerStandard p3 = new PassagerStandard("DescendArret5", 5);
        
        p1.monterDans(bus);
        p2.monterDans(bus);
        p3.monterDans(bus);
        
        bus.allerArretSuivant(); // Arrêt 1
        assertEquals(3, bus.getNbAssis());
        
        bus.allerArretSuivant(); // Arrêt 2 - 2 passagers descendent
        assertTrue(p1.estDehors());
        assertTrue(p2.estDehors());
        assertFalse(p3.estDehors());
        assertEquals(1, bus.getNbAssis());
        
        // Nouveaux passagers montent
        PassagerStandard p4 = new PassagerStandard("NouveauP4", 4);
        PassagerStandard p5 = new PassagerStandard("NouveauP5", 5);
        p4.monterDans(bus);
        p5.monterDans(bus);
        
        assertEquals(3, bus.getNbAssis());
    }

    // ========================================================================
    // SCÉNARIO 6 : Passagers avec même destination
    // ========================================================================

    @Test
    public void testPassagersMemeDestination() throws UsagerInvalideException {
        PassagerStandard[] groupe = new PassagerStandard[5];
        for (int i = 0; i < 5; i++) {
            groupe[i] = new PassagerStandard("Groupe" + i, 3);
            groupe[i].monterDans(bus);
        }
        
        // 3 assis, 2 debout
        int nbAssis = 0, nbDebout = 0;
        for (PassagerStandard p : groupe) {
            if (p.estAssis()) nbAssis++;
            if (p.estDebout()) nbDebout++;
        }
        assertEquals(3, nbAssis);
        assertEquals(2, nbDebout);
        
        // Avancer jusqu'à destination
        bus.allerArretSuivant(); // 1
        bus.allerArretSuivant(); // 2
        bus.allerArretSuivant(); // 3 - tous descendent
        
        for (PassagerStandard p : groupe) {
            assertTrue(p.nom() + " devrait être dehors", p.estDehors());
        }
        
        assertEquals(0, bus.getNbAssis());
        assertEquals(0, bus.getNbDebout());
    }

    // ========================================================================
    // SCÉNARIO 7 : Voyage longue distance
    // ========================================================================

    @Test
    public void testVoyageLongueDistance() throws UsagerInvalideException {
        PassagerStandard passager = new PassagerStandard("Voyageur", 100);
        
        passager.monterDans(bus);
        
        for (int i = 1; i < 100; i++) {
            bus.allerArretSuivant();
            assertFalse("Pas encore à destination (arrêt " + i + ")", passager.estDehors());
        }
        
        bus.allerArretSuivant(); // Arrêt 100
        assertTrue("Arrivé à destination", passager.estDehors());
    }

    // ========================================================================
    // SCÉNARIO 8 : Bus avec capacités asymétriques
    // ========================================================================

    @Test
    public void testBusCapacitesAsymetriques() throws UsagerInvalideException {
        Autobus busAsymetrique = new Autobus(1, 10); // 1 assis, 10 debout
        
        PassagerStandard[] passagers = new PassagerStandard[11];
        for (int i = 0; i < 11; i++) {
            passagers[i] = new PassagerStandard("P" + i, 20);
            passagers[i].monterDans(busAsymetrique);
        }
        
        assertEquals(1, busAsymetrique.getNbAssis());
        assertEquals(10, busAsymetrique.getNbDebout());
        assertTrue(passagers[0].estAssis()); // Premier assis
        for (int i = 1; i < 11; i++) {
            assertTrue("P" + i + " debout", passagers[i].estDebout());
        }
    }

    // ========================================================================
    // SCÉNARIO 9 : Rotation de passagers
    // ========================================================================

    @Test
    public void testRotationPassagers() throws UsagerInvalideException {
        Autobus busTresPetit = new Autobus(1, 0); // 1 seule place assise
        
        for (int tour = 1; tour <= 5; tour++) {
            PassagerStandard p = new PassagerStandard("Tour" + tour, tour);
            p.monterDans(busTresPetit);
            
            assertTrue("Passager tour " + tour + " assis", p.estAssis());
            assertEquals(1, busTresPetit.getNbAssis());
            
            busTresPetit.allerArretSuivant();
            assertTrue("Passager tour " + tour + " descendu", p.estDehors());
            assertEquals(0, busTresPetit.getNbAssis());
        }
    }

    // ========================================================================
    // SCÉNARIO 10 : Validations et exceptions
    // ========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testDestinationNegative() {
        new PassagerStandard("Invalid", -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNomNull() {
        new PassagerStandard(null, 5);
    }

    @Test
    public void testBusPleinException() throws UsagerInvalideException {
        Autobus miniature = new Autobus(0, 1);
        
        PassagerStandard p1 = new PassagerStandard("P1", 10);
        p1.monterDans(miniature);
        
        PassagerStandard p2 = new PassagerStandard("P2", 10);
        try {
            p2.monterDans(miniature);
            fail("Exception attendue");
        } catch (UsagerInvalideException e) {
            assertEquals("Aucune place disponible", e.getMessage());
            assertEquals(p2, e.quelUsager);
        }
    }

    // ========================================================================
    // TESTS DE COUVERTURE SUPPLÉMENTAIRES
    // ========================================================================

    @Test
    public void testToStringPassager() {
        PassagerStandard p = new PassagerStandard("TestToString", 5);
        String result = p.toString();
        assertTrue(result.contains("TestToString"));
        assertTrue(result.contains("dehors"));
        
        p.accepterPlaceAssise();
        result = p.toString();
        assertTrue(result.contains("assis"));
    }

    @Test
    public void testToStringBus() throws UsagerInvalideException {
        assertEquals("[arret:0, assis:0, debout:0]", bus.toString());
        
        PassagerStandard p = new PassagerStandard("Test", 10);
        bus.demanderPlaceAssise(p);
        assertEquals("[arret:0, assis:1, debout:0]", bus.toString());
        
        bus.allerArretSuivant();
        assertEquals("[arret:1, assis:1, debout:0]", bus.toString());
    }

    @Test
    public void testTransportFactory() throws UsagerInvalideException {
        Transport t = TransportFactory.creerAutobus(5, 10);
        assertNotNull(t);
        
        Usager u1 = TransportFactory.creerPassager("Test", 3);
        assertEquals("Test", u1.nom());
        
        Usager u2 = TransportFactory.creerPassager(7);
        assertEquals("PassagerStandard7", u2.nom());
    }
}
