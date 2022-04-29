package com.eulerhermes.dri.fileaid.Panels;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileSelectPanel extends JPanel implements ActionListener {

    // Composants du panneau
    private final JButton button = new JButton("Ouvrir un fichier");
    private final JLabel filePath = new JLabel("Pas de fichier choisi");
    private final JFileChooser chooser = new JFileChooser();

    // Donnée accéssible
    String fileName;

    public FileSelectPanel () {

        // on remplis le panneau
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(filePath);
        this.add(button);

        // on branche le bouton à une action
        button.addActionListener(this);

        // Initilaise le menu de selection du dossier
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Veuillez selectionner un fichier");
        //chooser.setFileSelectionMode((JFileChooser.FILES_ONLY));
        //chooser.setAcceptAllFileFilterUsed(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Si on a cliqué sur le bouton
        if(e.getSource().equals(button)) {

            // on demande à la selection de fichier de s'ouvrir et on contrôle son résultat
            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                // si la selection a été validée alors on met à jour le nom du fichier
                //filePath.setText(chooser.getSelectedFile().toString());
                filePath.setText(chooser.getSelectedFile().toString());
                fileName = chooser.getSelectedFile().getName();
            }
        }
    }
}
