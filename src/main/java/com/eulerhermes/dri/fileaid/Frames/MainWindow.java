package com.eulerhermes.dri.fileaid.Frames;

import com.eulerhermes.dri.fileaid.Panels.FileSelectPanel;
import com.eulerhermes.dri.fileaid.Panels.StructuredDataPanel;

import javax.swing.*;

public class MainWindow extends JFrame {

    /**
     * @author BGIRON
     * Public Constructor
     */
    public MainWindow() {
        // Titre de la fenêtre
        this.setTitle("File-Aid De Bogoss (si, si, j'assume ce titre)");
        // Taille de la fenêtre
        this.setSize(800,800);
        // Position de la fenêtre (null = centrée)
        this.setLocationRelativeTo(null);
        // Termine le processus quand on clique sur la croix rouge
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création des panneaux
        FileSelectPanel filesSelectionPanel = new FileSelectPanel();
        StructuredDataPanel dataPanel = new StructuredDataPanel();
        
        // Ajout de panneaux à la fenêtre
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.getContentPane().add(filesSelectionPanel);
        this.getContentPane().add(dataPanel);

        // Affichage de la fenêtre
        this.setVisible(true);
    }
}
