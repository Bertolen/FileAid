package com.eulerhermes.dri.fileaid.Panels;

import java.awt.*;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

// TODO faire que ce panneau déroulant déroule vraiment

public class StructuredDataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073619920286425563L;


	public StructuredDataPanel() {
		// l'affichage dans ce panneau se fait du haut vers le bas
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	/**
	 * Receptionne les données à afficher sous forme d'un tableau à deux dimentions
	 * @param structuredData liste à 2D des données à afficher
	 */
	public void setStructuredData(List<List<String>> structuredData) {
		
		// nettoyage du panneau deroulant
		this.removeAll();
		this.validate();
		
		// initialisation des données de la table
		DefaultTableModel tableModel = new DefaultTableModel(structuredData.get(0).toArray(), 0);

		structuredData.remove(0); // retrait de la première ligne qui nous a servi de header

		// alimente les données de la table
		for (List<String> ligne : structuredData) {
			tableModel.addRow(ligne.toArray());
		}

		// création de la table
		JTable table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // on lui dit de ne pas redimentionner la table
		resizeTable(table); // on redimentionne une seule fois à notre sauce et c'est tout.

		// ajoute la table au panneau
		JScrollPane panneauDeroulant = new JScrollPane(table);
		this.add(new JLabel("Résultats"));
		this.add(panneauDeroulant);
	}

	public void resizeTable(JTable table) {

		// Calcule la taille opti pour les colonnes
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0 ; i < table.getColumnCount() ; i++){ // boucle sur les colonnes
			int width = 15; // largeur minimale
			width = Math.max(table.getTableHeader().getColumnModel().getColumn(i).getPreferredWidth() , width);
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
