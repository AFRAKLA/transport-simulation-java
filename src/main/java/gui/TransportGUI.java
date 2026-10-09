package gui;

import tec.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface graphique simple (Swing) pour simuler le transport en commun.
 * 
 * Fonctionnalités :
 * - Créer un autobus avec capacités définies
 * - Ajouter des passagers avec destinations
 * - Simuler l'avancement arrêt par arrêt
 * - Visualiser l'état du bus et des passagers
 */
public class TransportGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    // Composants du bus
    private Autobus bus;
    private List<PassagerStandard> passagers;
    
    // Panels
    private JPanel busPanel;
    private JPanel passagerListPanel;
    private JTextArea logArea;
    
    // Labels d'état
    private JLabel lblArret;
    private JLabel lblAssis;
    private JLabel lblDebout;
    private JLabel lblPlacesAssises;
    private JLabel lblPlacesDebout;
    
    // Champs de saisie
    private JSpinner spinnerAssis;
    private JSpinner spinnerDebout;
    private JTextField txtNomPassager;
    private JSpinner spinnerDestination;
    
    // Boutons
    private JButton btnCreerBus;
    private JButton btnAjouterPassager;
    private JButton btnArretSuivant;
    private JButton btnReset;

    public TransportGUI() {
        super("Simulation de transport en commun");
        passagers = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de configuration (haut)
        add(createConfigPanel(), BorderLayout.NORTH);
        
        // Panel central avec l'état du bus et la liste des passagers
        add(createCenterPanel(), BorderLayout.CENTER);
        
        // Panel de log (bas)
        add(createLogPanel(), BorderLayout.SOUTH);
        
        updateUI();
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Configuration du Bus",
            TitledBorder.LEFT, TitledBorder.TOP));

        // Configuration du bus
        panel.add(new JLabel("Places assises:"));
        spinnerAssis = new JSpinner(new SpinnerNumberModel(3, 0, 50, 1));
        panel.add(spinnerAssis);

        panel.add(new JLabel("Places debout:"));
        spinnerDebout = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        panel.add(spinnerDebout);

        btnCreerBus = new JButton("Créer Bus");
        // Pas de texte blanc : l'apparence Windows ignore setBackground,
        // ce qui donnait du blanc sur gris clair, illisible.
        btnCreerBus.addActionListener(this::creerBus);
        panel.add(btnCreerBus);

        panel.add(Box.createHorizontalStrut(30));

        // Ajout de passager
        panel.add(new JLabel("Nom:"));
        txtNomPassager = new JTextField(10);
        panel.add(txtNomPassager);

        panel.add(new JLabel("Destination:"));
        spinnerDestination = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        panel.add(spinnerDestination);

        btnAjouterPassager = new JButton("Ajouter Passager");
        btnAjouterPassager.setEnabled(false);
        btnAjouterPassager.addActionListener(this::ajouterPassager);
        panel.add(btnAjouterPassager);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel du bus (gauche)
        busPanel = createBusStatusPanel();
        panel.add(busPanel, BorderLayout.WEST);

        // Liste des passagers (centre)
        JPanel passagerPanel = new JPanel(new BorderLayout());
        passagerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Passagers",
            TitledBorder.LEFT, TitledBorder.TOP));
        
        passagerListPanel = new JPanel();
        passagerListPanel.setLayout(new BoxLayout(passagerListPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(passagerListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        passagerPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(passagerPanel, BorderLayout.CENTER);

        // Boutons de contrôle (droite)
        panel.add(createControlPanel(), BorderLayout.EAST);

        return panel;
    }

    private JPanel createBusStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "État du Bus",
            TitledBorder.LEFT, TitledBorder.TOP));
        panel.setPreferredSize(new Dimension(200, 300));

        // Icône du bus
        JLabel busIcon = new JLabel("🚌", SwingConstants.CENTER);
        busIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        busIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(busIcon);
        panel.add(Box.createVerticalStrut(20));

        // Informations
        lblArret = new JLabel("Arrêt: -");
        lblArret.setFont(new Font("Arial", Font.BOLD, 16));
        lblArret.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblArret);
        panel.add(Box.createVerticalStrut(10));

        lblPlacesAssises = new JLabel("Capacité assise: -");
        lblPlacesAssises.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPlacesAssises);

        lblAssis = new JLabel("Passagers assis: 0");
        lblAssis.setForeground(new Color(76, 175, 80));
        lblAssis.setFont(new Font("Arial", Font.BOLD, 14));
        lblAssis.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblAssis);
        panel.add(Box.createVerticalStrut(10));

        lblPlacesDebout = new JLabel("Capacité debout: -");
        lblPlacesDebout.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPlacesDebout);

        lblDebout = new JLabel("Passagers debout: 0");
        lblDebout.setForeground(new Color(255, 152, 0));
        lblDebout.setFont(new Font("Arial", Font.BOLD, 14));
        lblDebout.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblDebout);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Contrôles",
            TitledBorder.LEFT, TitledBorder.TOP));
        panel.setPreferredSize(new Dimension(150, 300));

        btnArretSuivant = new JButton("Arrêt Suivant ➡");
        btnArretSuivant.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnArretSuivant.setMaximumSize(new Dimension(140, 40));
        btnArretSuivant.setEnabled(false);
        btnArretSuivant.addActionListener(this::allerArretSuivant);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnArretSuivant);

        panel.add(Box.createVerticalStrut(20));

        btnReset = new JButton("Reset 🔄");
        btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReset.setMaximumSize(new Dimension(140, 40));
        btnReset.addActionListener(this::reset);
        panel.add(btnReset);

        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Journal des événements",
            TitledBorder.LEFT, TitledBorder.TOP));

        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ========================================================================
    // Actions
    // ========================================================================

    private void creerBus(ActionEvent e) {
        int nbAssis = (Integer) spinnerAssis.getValue();
        int nbDebout = (Integer) spinnerDebout.getValue();
        
        try {
            bus = new Autobus(nbAssis, nbDebout);
            passagers.clear();
            
            log("✓ Bus créé avec " + nbAssis + " places assises et " + nbDebout + " places debout");
            
            btnAjouterPassager.setEnabled(true);
            btnArretSuivant.setEnabled(true);
            btnCreerBus.setEnabled(false);
            spinnerAssis.setEnabled(false);
            spinnerDebout.setEnabled(false);
            
            updateUI();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la création du bus: " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterPassager(ActionEvent e) {
        if (bus == null) return;
        
        String nom = txtNomPassager.getText().trim();
        if (nom.isEmpty()) {
            nom = "Passager" + (passagers.size() + 1);
        }
        
        int destination = (Integer) spinnerDestination.getValue();
        
        try {
            PassagerStandard passager = new PassagerStandard(nom, destination);
            passager.monterDans(bus);
            passagers.add(passager);
            
            log("✓ " + nom + " monte dans le bus (destination: arrêt " + destination + ") - " + 
                (passager.estAssis() ? "ASSIS" : "DEBOUT"));
            
            txtNomPassager.setText("");
            updateUI();
        } catch (UsagerInvalideException ex) {
            log("✗ " + nom + " ne peut pas monter: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Impossible de monter: " + ex.getMessage(),
                "Bus plein", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void allerArretSuivant(ActionEvent e) {
        if (bus == null) return;
        
        int arretPrecedent = bus.getNumeroArret();
        int nbAvant = bus.getNbAssis() + bus.getNbDebout();
        
        try {
            bus.allerArretSuivant();
            
            int nbApres = bus.getNbAssis() + bus.getNbDebout();
            int descendus = nbAvant - nbApres;
            
            log("➡ Arrêt " + bus.getNumeroArret() + " atteint" + 
                (descendus > 0 ? " - " + descendus + " passager(s) descendu(s)" : ""));
            
            // Log des passagers descendus
            for (PassagerStandard p : passagers) {
                if (p.estDehors() && p.getDestination() == bus.getNumeroArret()) {
                    log("   ↓ " + p.nom() + " est descendu à destination");
                }
            }
            
            updateUI();
        } catch (UsagerInvalideException ex) {
            log("✗ Erreur à l'arrêt suivant: " + ex.getMessage());
        }
    }

    private void reset(ActionEvent e) {
        bus = null;
        passagers.clear();
        
        btnCreerBus.setEnabled(true);
        spinnerAssis.setEnabled(true);
        spinnerDebout.setEnabled(true);
        btnAjouterPassager.setEnabled(false);
        btnArretSuivant.setEnabled(false);
        
        log("🔄 Simulation réinitialisée");
        updateUI();
    }

    // ========================================================================
    // Mise à jour de l'interface
    // ========================================================================

    private void updateUI() {
        // Mise à jour du panel bus
        if (bus != null) {
            lblArret.setText("Arrêt: " + bus.getNumeroArret());
            lblAssis.setText("Passagers assis: " + bus.getNbAssis());
            lblDebout.setText("Passagers debout: " + bus.getNbDebout());
            lblPlacesAssises.setText("Capacité assise: " + spinnerAssis.getValue());
            lblPlacesDebout.setText("Capacité debout: " + spinnerDebout.getValue());
        } else {
            lblArret.setText("Arrêt: -");
            lblAssis.setText("Passagers assis: -");
            lblDebout.setText("Passagers debout: -");
            lblPlacesAssises.setText("Capacité assise: -");
            lblPlacesDebout.setText("Capacité debout: -");
        }
        
        // Mise à jour de la liste des passagers
        passagerListPanel.removeAll();
        
        for (PassagerStandard p : passagers) {
            JPanel pPanel = createPassagerPanel(p);
            passagerListPanel.add(pPanel);
            passagerListPanel.add(Box.createVerticalStrut(5));
        }
        
        passagerListPanel.revalidate();
        passagerListPanel.repaint();
    }

    private JPanel createPassagerPanel(PassagerStandard p) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Icône selon l'état
        String icon;
        Color bgColor;
        if (p.estDehors()) {
            icon = "🚶";
            bgColor = new Color(200, 200, 200);
        } else if (p.estAssis()) {
            icon = "🪑";
            bgColor = new Color(200, 230, 200);
        } else {
            icon = "🧍";
            bgColor = new Color(255, 230, 200);
        }
        
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        panel.add(lblIcon);
        
        String etat = p.estDehors() ? "DEHORS" : (p.estAssis() ? "ASSIS" : "DEBOUT");
        JLabel lblInfo = new JLabel(p.nom() + " | Dest: " + p.getDestination() + " | " + etat);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblInfo);
        
        return panel;
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ========================================================================
    // Main
    // ========================================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Utiliser le look and feel par défaut
            }
            
            TransportGUI gui = new TransportGUI();
            gui.setVisible(true);
        });
    }
}
