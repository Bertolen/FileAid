package com.eulerhermes.dri.fileaid.Panels;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StructuredDataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073619920286425563L;
	
	JScrollPane panneauDeroulant = new JScrollPane();
	
	public StructuredDataPanel() {
		// l'affichage dans ce panneau se fait du haut vers le bas
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// 
		this.add(new JLabel("Résultats"));
		this.add(panneauDeroulant);
	}
	
	/**
	 * Receptionne les données à afficher sous forme d'un tableau à deux dimentions
	 * @param structuredData
	 */
	public void setStructuredData(ArrayList<ArrayList<String>> structuredData) {

		// vide la table
		panneauDeroulant.removeAll();
		panneauDeroulant.validate();
		
		// créé une nouvelle table
		DefaultTableModel table = new DefaultTableModel(structuredData.get(0).toArray(), 0);
		
		// retire la première ligne (on l'a ajouté en entête)
		structuredData.remove(0);
		
		// alimente la nouvelle table
		for (ArrayList<String> ligne : structuredData) {
			table.addRow(ligne.toArray());
		}
		

		panneauDeroulant.add(new JTable(table));
	}

}
