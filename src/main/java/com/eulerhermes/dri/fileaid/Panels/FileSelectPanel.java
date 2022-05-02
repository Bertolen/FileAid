package com.eulerhermes.dri.fileaid.Panels;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;

import com.eulerhermes.dri.fileaid.DataParsing.DataParsing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileSelectPanel extends JPanel implements ActionListener {

    // Composants du panneau
    private final JButton copyButton = new JButton();
    private final JButton dataButton = new JButton();
    private final JLabel copyFilePath = new JLabel("Pas de fichier choisi");
    private final JLabel dataFilePath = new JLabel("Pas de fichier choisi");
    private final JFileChooser chooser = new JFileChooser();

    // Référence au contrôleur de données
    DataParsing dataParser;
    
    public FileSelectPanel () {
    	
    	// Personnalisation des textes des deux boutons
    	copyButton.setText("Ouvrir une copy");
    	dataButton.setText("Ouvrir un fichier");
    	
        // on remplis le panneau des copy
    	JPanel copyPanel = new JPanel();
    	copyPanel.setLayout(new BoxLayout(copyPanel, BoxLayout.PAGE_AXIS));
    	copyPanel.add(copyFilePath);
    	copyPanel.add(copyButton);
    	
        // on remplis le panneau des données
    	JPanel dataPanel = new JPanel();
    	dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.PAGE_AXIS));
    	dataPanel.add(dataFilePath);
    	dataPanel.add(dataButton);
    	
    	// ajout des deux sous-panneaux
    	this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    	this.add(copyPanel);
    	this.add(dataPanel);

        // on branche les bouton à une action
        copyButton.addActionListener(this);
        dataButton.addActionListener(this);

        // Initilaise le menu de selection du dossier
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Veuillez selectionner un fichier");
        chooser.setFileSelectionMode((JFileChooser.FILES_ONLY));
        chooser.setAcceptAllFileFilterUsed(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Si on a cliqué sur le bouton des copy
        if(e.getSource().equals(copyButton)) {

            // on demande à la selection de fichier de s'ouvrir et on contrôle son résultat
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                // si la selection a été validée alors on met à jour le nom du fichier
                copyFilePath.setText(chooser.getSelectedFile().toString());
                dataParser.setCopy(chooser.getSelectedFile());
            }
        }

        // Si on a cliqué sur le bouton des fichiers
        if(e.getSource().equals(dataButton)) {

            // on demande à la selection de fichier de s'ouvrir et on contrôle son résultat
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                // si la selection a été validée alors on met à jour le nom du fichier
            	dataFilePath.setText(chooser.getSelectedFile().toString());
                dataParser.setData(chooser.getSelectedFile());
            }
        }
    }

    public void setDataParser(DataParsing dataParser) {
    	this.dataParser = dataParser;
    }
}
