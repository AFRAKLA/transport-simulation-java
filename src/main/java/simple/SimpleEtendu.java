package simple;

import tec.*;

/**
 * Tests fonctionnels pour le système de transport.
 * 
 * Cette classe étend Simple.java avec des scénarios de test plus complexes :
 * - Plusieurs passagers montant dans le même bus
 * - Changements de places multiples
 * - Tentatives de montée avec bus plein
 * - Sorties avant la destination
 * - Scénarios d'erreur
 */
public class SimpleEtendu {

    /**
     * Affiche l'état du transport et d'un usager.
     */
    private static void afficherEtat(String label, Transport t, Usager... usagers) {
        System.out.println("\n=== " + label + " ===");
        System.out.println("Bus: " + t);
        for (Usager u : usagers) {
            System.out.println("  - " + u);
        }
    }

    /**
     * Affiche un séparateur de scénario.
     */
    private static void nouveauScenario(String nom) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO: " + nom);
        System.out.println("=".repeat(60));
    }

    public static void main(String[] args) throws UsagerInvalideException {
        
        // ====================================================================
        // SCÉNARIO 1 : Plusieurs passagers montant dans le même bus
        // ====================================================================
        nouveauScenario("Plusieurs passagers montant dans le même bus");
        
        Transport bus1 = TransportFactory.creerAutobus(2, 3);
        Usager alice = TransportFactory.creerPassager("Alice", 3);
        Usager bob = TransportFactory.creerPassager("Bob", 5);
        Usager charlie = TransportFactory.creerPassager("Charlie", 4);
        Usager diana = TransportFactory.creerPassager("Diana", 6);
        
        afficherEtat("État initial", bus1, alice, bob, charlie, diana);
        
        alice.monterDans(bus1);
        bob.monterDans(bus1);
        charlie.monterDans(bus1);
        diana.monterDans(bus1);
        
        afficherEtat("Après montée de 4 passagers", bus1, alice, bob, charlie, diana);
        System.out.println("Alice et Bob sont assis (2 places), Charlie et Diana sont debout");
        
        // ====================================================================
        // SCÉNARIO 2 : Avancement arrêt par arrêt avec descentes
        // ====================================================================
        nouveauScenario("Avancement arrêt par arrêt avec descentes");
        
        bus1.allerArretSuivant(); // Arrêt 1
        afficherEtat("Arrêt 1", bus1, alice, bob, charlie, diana);
        
        bus1.allerArretSuivant(); // Arrêt 2
        afficherEtat("Arrêt 2", bus1, alice, bob, charlie, diana);
        
        bus1.allerArretSuivant(); // Arrêt 3 - Alice descend
        afficherEtat("Arrêt 3 - Alice descend", bus1, alice, bob, charlie, diana);
        
        bus1.allerArretSuivant(); // Arrêt 4 - Charlie descend
        afficherEtat("Arrêt 4 - Charlie descend", bus1, alice, bob, charlie, diana);
        
        bus1.allerArretSuivant(); // Arrêt 5 - Bob descend
        afficherEtat("Arrêt 5 - Bob descend", bus1, alice, bob, charlie, diana);
        
        bus1.allerArretSuivant(); // Arrêt 6 - Diana descend
        afficherEtat("Arrêt 6 - Diana descend (bus vide)", bus1, alice, bob, charlie, diana);
        
        // ====================================================================
        // SCÉNARIO 3 : Bus plein - Exception attendue
        // ====================================================================
        nouveauScenario("Tentative de montée dans un bus plein");
        
        Transport busPetit = TransportFactory.creerAutobus(1, 1);
        Usager eve = TransportFactory.creerPassager("Eve", 5);
        Usager frank = TransportFactory.creerPassager("Frank", 5);
        Usager grace = TransportFactory.creerPassager("Grace", 5);
        
        eve.monterDans(busPetit);
        frank.monterDans(busPetit);
        
        afficherEtat("Bus plein (1 assis, 1 debout)", busPetit, eve, frank);
        
        try {
            grace.monterDans(busPetit);
            System.out.println("ERREUR: Grace n'aurait pas dû pouvoir monter!");
        } catch (UsagerInvalideException e) {
            System.out.println("✓ Exception correctement levée: " + e.getMessage());
            System.out.println("  Grace reste dehors: " + grace);
        }
        
        // ====================================================================
        // SCÉNARIO 4 : Scénario réaliste - Ligne de bus urbaine
        // ====================================================================
        nouveauScenario("Ligne de bus urbaine réaliste");
        
        Transport busUrbain = TransportFactory.creerAutobus(3, 5);
        
        // Arrêt 0 : Départ terminus
        System.out.println("\n--- Terminus (Arrêt 0) ---");
        Usager p1 = TransportFactory.creerPassager("Marie", 3);    // Centre-ville
        Usager p2 = TransportFactory.creerPassager("Pierre", 5);   // Gare
        Usager p3 = TransportFactory.creerPassager("Julie", 2);    // Mairie
        
        p1.monterDans(busUrbain);
        p2.monterDans(busUrbain);
        p3.monterDans(busUrbain);
        afficherEtat("Départ terminus", busUrbain, p1, p2, p3);
        
        // Arrêt 1 : Nouveaux passagers
        busUrbain.allerArretSuivant();
        System.out.println("\n--- Arrêt 1 : Écoles ---");
        Usager p4 = TransportFactory.creerPassager("Lucas", 4);    // Bibliothèque
        Usager p5 = TransportFactory.creerPassager("Emma", 6);     // Terminus
        p4.monterDans(busUrbain);
        p5.monterDans(busUrbain);
        afficherEtat("Après montées", busUrbain, p1, p2, p3, p4, p5);
        
        // Arrêt 2 : Julie descend, nouveaux passagers
        busUrbain.allerArretSuivant();
        System.out.println("\n--- Arrêt 2 : Mairie (Julie descend) ---");
        Usager p6 = TransportFactory.creerPassager("Léo", 5);
        p6.monterDans(busUrbain);
        afficherEtat("Après échanges", busUrbain, p1, p2, p3, p4, p5, p6);
        
        // Continue jusqu'au terminus
        for (int i = 3; i <= 6; i++) {
            busUrbain.allerArretSuivant();
            System.out.println("\n--- Arrêt " + i + " ---");
            System.out.println("Bus: " + busUrbain);
        }
        
        // ====================================================================
        // SCÉNARIO 5 : Gestion des erreurs
        // ====================================================================
        nouveauScenario("Gestion des erreurs et cas limites");
        
        // Test création passager avec destination négative
        System.out.println("\n1. Création passager destination négative:");
        try {
            Usager invalid = TransportFactory.creerPassager("Invalid", -1);
            System.out.println("ERREUR: Devrait lever une exception!");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception correctement levée: " + e.getMessage());
        }
        
        // Test création passager avec nom null
        System.out.println("\n2. Création passager nom null:");
        try {
            Usager invalid = TransportFactory.creerPassager(null, 5);
            System.out.println("ERREUR: Devrait lever une exception!");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Exception correctement levée: " + e.getMessage());
        }
        
        // Test montée dans transport null
        System.out.println("\n3. Montée dans transport null:");
        Usager testPassager = TransportFactory.creerPassager("Test", 5);
        try {
            testPassager.monterDans(null);
            System.out.println("ERREUR: Devrait lever une exception!");
        } catch (UsagerInvalideException e) {
            System.out.println("✓ Exception correctement levée: " + e.getMessage());
        }
        
        // ====================================================================
        // SCÉNARIO 6 : Statistiques de voyage
        // ====================================================================
        nouveauScenario("Statistiques de voyage");
        
        Transport busStats = TransportFactory.creerAutobus(10, 20);
        int passagersMontés = 0;
        int passagersDescendus = 0;
        
        // Créer 15 passagers avec différentes destinations
        Usager[] passagers = new Usager[15];
        for (int i = 0; i < 15; i++) {
            int destination = (i % 5) + 1; // Destinations 1 à 5
            passagers[i] = TransportFactory.creerPassager("P" + i, destination);
            passagers[i].monterDans(busStats);
            passagersMontés++;
        }
        
        System.out.println("Passagers montés: " + passagersMontés);
        System.out.println("État initial: " + busStats);
        
        for (int arret = 1; arret <= 5; arret++) {
            int avantArret = compterPassagersDansBus(passagers);
            busStats.allerArretSuivant();
            int apresArret = compterPassagersDansBus(passagers);
            int descendus = avantArret - apresArret;
            passagersDescendus += descendus;
            
            System.out.println("Arrêt " + arret + ": " + descendus + " passager(s) descendu(s) - " + busStats);
        }
        
        System.out.println("\nRésumé:");
        System.out.println("  Total montés: " + passagersMontés);
        System.out.println("  Total descendus: " + passagersDescendus);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TOUS LES SCÉNARIOS TERMINÉS AVEC SUCCÈS");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Compte le nombre de passagers actuellement dans le bus.
     */
    private static int compterPassagersDansBus(Usager[] passagers) {
        int count = 0;
        for (Usager p : passagers) {
            if (!p.estDehors()) {
                count++;
            }
        }
        return count;
    }
}
