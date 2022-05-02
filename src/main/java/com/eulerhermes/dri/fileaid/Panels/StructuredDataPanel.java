package com.eulerhermes.dri.fileaid.Panels;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StructuredDataPanel extends JPanel {

	private String[] tableHeaders = new String[]{"Nom du champ", "Valeur du champ"};
	private final DefaultTableModel table = new DefaultTableModel(tableHeaders, 0);
	
	public StructuredDataPanel() {

		// Ajout de notre table dans le panneau
		this.add(new JTable(table));
		
	}

}
