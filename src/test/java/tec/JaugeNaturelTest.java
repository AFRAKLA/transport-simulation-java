package tec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests unitaires pour JaugeNaturel.
 * 
 * Ces tests assurent une couverture complète de la classe JaugeNaturel.
 */
public class JaugeNaturelTest {

    // ========================================================================
    // TESTS DE CONSTRUCTION
    // ========================================================================

    @Test
    public void testConstructeurValide() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 5);
        
        assertEquals(5, jauge.getNiveau());
        assertFalse(jauge.estVert());
        assertFalse(jauge.estRouge());
    }

    @Test
    public void testConstructeurNiveauMin() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 0);
        
        assertTrue("Devrait être vert (au minimum)", jauge.estVert());
        assertFalse(jauge.estRouge());
    }

    @Test
    public void testConstructeurNiveauMax() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 10);
        
        assertFalse(jauge.estVert());
        assertTrue("Devrait être rouge (au maximum)", jauge.estRouge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructeurMinSuperieurMax() {
        new JaugeNaturel(10, 5, 7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructeurNiveauSousMin() {
        new JaugeNaturel(5, 10, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructeurNiveauSurMax() {
        new JaugeNaturel(0, 10, 15);
    }

    // ========================================================================
    // TESTS D'INCRÉMENTATION
    // ========================================================================

    @Test
    public void testIncrementer() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 5);
        
        jauge.incrementer();
        assertEquals(6, jauge.getNiveau());
    }

    @Test
    public void testIncrementerAuMax() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 10);
        
        jauge.incrementer(); // Ne doit pas dépasser le max
        assertEquals(10, jauge.getNiveau());
        assertTrue(jauge.estRouge());
    }

    @Test
    public void testIncrementerPlusieursEtapes() {
        JaugeNaturel jauge = new JaugeNaturel(0, 5, 0);
        
        for (int i = 1; i <= 5; i++) {
            jauge.incrementer();
            assertEquals(i, jauge.getNiveau());
        }
        
        assertTrue(jauge.estRouge());
        
        // Incrémentation supplémentaire ne change rien
        jauge.incrementer();
        assertEquals(5, jauge.getNiveau());
    }

    // ========================================================================
    // TESTS DE DÉCRÉMENTATION
    // ========================================================================

    @Test
    public void testDecrementer() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 5);
        
        jauge.decrementer();
        assertEquals(4, jauge.getNiveau());
    }

    @Test
    public void testDecrementerAuMin() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 0);
        
        jauge.decrementer(); // Ne doit pas descendre sous le min
        assertEquals(0, jauge.getNiveau());
        assertTrue(jauge.estVert());
    }

    @Test
    public void testDecrementerPlusieursEtapes() {
        JaugeNaturel jauge = new JaugeNaturel(0, 5, 5);
        
        for (int i = 4; i >= 0; i--) {
            jauge.decrementer();
            assertEquals(i, jauge.getNiveau());
        }
        
        assertTrue(jauge.estVert());
        
        // Décrémentation supplémentaire ne change rien
        jauge.decrementer();
        assertEquals(0, jauge.getNiveau());
    }

    // ========================================================================
    // TESTS DES ÉTATS
    // ========================================================================

    @Test
    public void testEstVertAuMinimum() {
        JaugeNaturel jauge = new JaugeNaturel(3, 10, 3);
        assertTrue(jauge.estVert());
    }

    @Test
    public void testEstVertFauxAuDessusMin() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 1);
        assertFalse(jauge.estVert());
    }

    @Test
    public void testEstRougeAuMaximum() {
        JaugeNaturel jauge = new JaugeNaturel(0, 7, 7);
        assertTrue(jauge.estRouge());
    }

    @Test
    public void testEstRougeFauxEnDessousMax() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 9);
        assertFalse(jauge.estRouge());
    }

    // ========================================================================
    // TESTS AVEC VALEURS NÉGATIVES
    // ========================================================================

    @Test
    public void testJaugeValeurNegatives() {
        JaugeNaturel jauge = new JaugeNaturel(-10, -2, -5);
        
        assertEquals(-5, jauge.getNiveau());
        assertFalse(jauge.estVert());
        assertFalse(jauge.estRouge());
    }

    @Test
    public void testJaugeNegatifIncrementer() {
        JaugeNaturel jauge = new JaugeNaturel(-5, 0, -3);
        
        jauge.incrementer();
        assertEquals(-2, jauge.getNiveau());
    }

    @Test
    public void testJaugeNegatifDecrementer() {
        JaugeNaturel jauge = new JaugeNaturel(-5, 0, -3);
        
        jauge.decrementer();
        assertEquals(-4, jauge.getNiveau());
    }

    // ========================================================================
    // TESTS DE LIMITES
    // ========================================================================

    @Test
    public void testJaugeMinEgalMax() {
        JaugeNaturel jauge = new JaugeNaturel(5, 5, 5);
        
        assertTrue(jauge.estVert());
        assertTrue(jauge.estRouge());
        assertEquals(5, jauge.getNiveau());
        
        jauge.incrementer();
        assertEquals(5, jauge.getNiveau());
        
        jauge.decrementer();
        assertEquals(5, jauge.getNiveau());
    }

    @Test
    public void testJaugeZeroCapacite() {
        JaugeNaturel jauge = new JaugeNaturel(0, 0, 0);
        
        assertTrue(jauge.estVert());
        assertTrue(jauge.estRouge());
    }

    // ========================================================================
    // TESTS DE toString
    // ========================================================================

    @Test
    public void testToString() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 5);
        String result = jauge.toString();
        
        assertTrue(result.contains("min:0"));
        assertTrue(result.contains("max:10"));
        assertTrue(result.contains("niveau:5"));
    }

    // ========================================================================
    // TESTS DE SÉQUENCES COMPLEXES
    // ========================================================================

    @Test
    public void testSequenceIncrementerDecrementer() {
        JaugeNaturel jauge = new JaugeNaturel(0, 10, 5);
        
        jauge.incrementer(); // 6
        jauge.incrementer(); // 7
        jauge.decrementer(); // 6
        jauge.incrementer(); // 7
        jauge.decrementer(); // 6
        jauge.decrementer(); // 5
        
        assertEquals(5, jauge.getNiveau());
    }
}
