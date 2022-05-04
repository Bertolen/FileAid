package com.eulerhermes.dri.fileaid.Panels;

import java.util.List;

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

	
	private final JScrollPane panneauDeroulant = new JScrollPane();
	
	public StructuredDataPanel() {
		// l'affichage dans ce panneau se fait du haut vers le bas
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// ajoute les elements à l'objet
		this.add(new JLabel("Résultats"));
		this.add(panneauDeroulant);
	}
	
	/**
	 * Receptionne les données à afficher sous forme d'un tableau à deux dimentions
	 * @param structuredData
	 */
	public void setStructuredData(List<List<String>> structuredData) {
		
		// nettoyage du panneau deroulant
		panneauDeroulant.removeAll();
		panneauDeroulant.validate();
		
		// initialisation de la table
		DefaultTableModel tableModel = new DefaultTableModel(structuredData.get(0).toArray(), 0);
		structuredData.remove(0);
		
		// alimente la nouvelle table
		for (List<String> ligne : structuredData) {
			tableModel.addRow(ligne.toArray());
			System.out.println("Ajoute la ligne : " + ligne.toString());
		}

		// ajoute la table au panneau
		JTable table = new JTable(tableModel);
		panneauDeroulant.add(table);
	}

}
