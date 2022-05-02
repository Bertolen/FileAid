package com.eulerhermes.dri.fileaid.DataParsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.eulerhermes.dri.fileaid.Panels.StructuredDataPanel;
import com.eulerhermes.dri.fileaid.model.DataInfo;

public class DataParsing {

	private StructuredDataPanel dataPanel = null;
	private File copyFile = null;
	private File dataFile = null;
	
	/**
	 * Indique le panneau de données pour afficher le résultat
	 * @param dataPanel
	 */
	public void setDataPanel(StructuredDataPanel dataPanel) {
		this.dataPanel = dataPanel;
	}
	
	/**
	 * Met à jour le fichier de la copy
	 * @param copyFile fichier de la copy
	 */
	public void setCopy(File copyFile) {
		this.copyFile = copyFile;
		
		// si on a les deux fichiers alors on les parse
		if(this.dataPanel != null) parseData();
	}
	

	/**
	 * Met à jour le chemin du fichier de données
	 * @param dataFile chemin du fichier de données
	 */
	public void setData(File dataFile) {
		this.dataFile = dataFile;
		
		// si on a les deux fichiers alors on les parse
		if(this.copyFile != null) parseData();
	}
	
	private ArrayList<DataInfo> parseCopy() {
		// Ouverture de la copy
		BufferedReader copyReader;
		try {
			copyReader = new BufferedReader(new InputStreamReader(new FileInputStream(copyFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		// initialisation du tableau de retour
		ArrayList<DataInfo> copyInfo = new ArrayList<DataInfo>();
		try {

			// Boucle sur chaque ligne
			String copyLine;
			copyLine = copyReader.readLine();
			while (copyLine != null) {
				
				// si le 7ème caractère est un asterix alors on ignore la ligne, c'est un commentaire
				if(copyLine.charAt(6) != '*') {
				
					// on retire les 7 premiers caractères et les espaces en trop devant et après les infos utiles
					copyLine = copyLine.substring(7).trim();
					
					// vérifie si on est dans une variable groupée ou pas
					boolean variableGroupe = copyLine.toUpperCase().contains("PIC");
				
					// si on n'est pas dans une variable groupée on ajoute son nom, sa taille et sa position
					if(!variableGroupe) {
						
						DataInfo lineInfo = new DataInfo();
						//
						copyInfo.add(lineInfo);
					}
				}
				
				// prochaine lecture
				copyLine = copyReader.readLine();
			}
			
			// ferme le fichier
			copyReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// renvoi le résultat
		return copyInfo;
	}
	
	/**
	 * Lis la copy et le fichier de données pour déterminer les valeurs du fichier de données
	 */
	public void parseData(){

		// interpretation de la copy
		ArrayList<DataInfo> copyInfo = parseCopy();
		
		// interpretation des données
		
	}
}
