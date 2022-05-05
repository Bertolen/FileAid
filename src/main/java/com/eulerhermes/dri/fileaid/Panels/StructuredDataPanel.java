package com.eulerhermes.dri.fileaid.Panels;

import java.awt.*;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

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
	 * @param structuredData liste à 2D des données à afficher
	 */
	public void setStructuredData(List<List<String>> structuredData) {
		
		// nettoyage du panneau deroulant
		panneauDeroulant.removeAll();
		panneauDeroulant.validate();
		
		// initialisation de la table
		DefaultTableModel tableModel = new DefaultTableModel(0, structuredData.get(0).size());

		// alimente la nouvelle table
		for (List<String> ligne : structuredData) {
			tableModel.addRow(ligne.toArray());
		}

		// ajoute la table au panneau
		JTable table = new JTable(tableModel);
		panneauDeroulant.add(table);

		// Redimentionne le tableau pour le rendre visible
	}

	public void resizeTable(JTable table) {

		// Calcule la taille opti pour les colonnes
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0 ; i < table.getColumnCount() ; i++){ // boucle sur les colonnes
			int width = 15; // largeur minimale
			for (int j = 0 ; j < table.getRowCount() ; j++){ // boucle sur les lignes
				TableCellRenderer renderer = table.getCellRenderer(j,i);
				Component comp = table.prepareRenderer(renderer,j,i);
				width = Math.max(comp.getPreferredSize().width + 1 , width);
			}
			if(width > 300) width = 300;// largeur maximale
			columnModel.getColumn(i).setPreferredWidth(width);
		}

		// applique la taille opti
		table.setSize(table.getPreferredSize());

	}

}
