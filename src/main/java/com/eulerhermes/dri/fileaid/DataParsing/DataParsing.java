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
import com.eulerhermes.dri.fileaid.model.TypeEnum;

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
		if(this.dataFile != null) parseData();
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
	
	/**
	 * parcours la copy pour en déduire la position de chaque donnée
	 * !!! ATTENTION !!!! Pour l'instant on considère uniquement les données définies sur une seule ligne et on ne traite pas les redefine
	 * @return liste de DataInfo où chaque element correspond à une donnée de la copy
	 */
	
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

			// position initiale
			int position = 1;
			
			// Boucle sur chaque ligne
			String copyLine;
			copyLine = copyReader.readLine();
			while (copyLine != null) {
				
				// si le 7ème caractère est un asterix alors on ignore la ligne, c'est un commentaire
				if(copyLine.charAt(6) != '*') {
				
					// on retire les 7 premiers caractères et les espaces en trop devant et après les infos utiles
					copyLine = copyLine.substring(7).trim();
					
					// vérifie si on est dans une variable groupée ou pas
					boolean variableGroupe = !copyLine.toUpperCase().contains("PIC");
				
					// si on n'est pas dans une variable groupée on ajoute son nom, sa taille et sa position
					if(!variableGroupe) {
						
						// Découpe de la ligne en mots séparés par des espaces
						String[] words = copyLine.split(" ");
						
						// alimentation du nouveau DataInfo
						DataInfo lineInfo = new DataInfo();
						lineInfo.setName(words[1]); // ATENTION : pour l'instant on considère que le nom est toujours la deuxième valeur
						lineInfo.setType(typeOf(words[3]));
						lineInfo.setSize(computeSize(words[3], lineInfo.getType()));
						lineInfo.setPosition(position);
						copyInfo.add(lineInfo);
						
						// incrémentation de la position
						position += lineInfo.getSize();
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
	 * Determine le type d'une donnée à partir de sa déclaration
	 * @param sizeText déclaration du type de donnée en cobol
	 * @return le type de donnée dans un TypeEnum
	 */
	private TypeEnum typeOf(String sizeText) {
		// detection du type alphanumerique
		if(sizeText.contains("X")) return TypeEnum.ALPHANUM;
		
		// detection du type numerique décimal
		if(sizeText.contains("V")) return TypeEnum.FLOAT;
		
		// par élimination le reste c'est du numérique entier
		return TypeEnum.INTEGER;
	}
	
	/**
	 * calcule la taille d'un champ cobol
	 * @param sizeText taille du champ en cobol
	 * @return taille du champ en nombre de caractères
	 */
	private int computeSize(String sizeText, TypeEnum type) {
		
		switch(type) {
		case ALPHANUM: // cas des variables alphanumériques
			// si on a une version simplifiée 
			if(sizeText.startsWith("X("))
				return Integer.parseInt(sizeText.substring(2, sizeText.indexOf(')')));
			
			// version détaillée
			return 1; // TODO
			
		case INTEGER:
			// si on a une version simplifiée 
			if(sizeText.startsWith("9("))
				return Integer.parseInt(sizeText.substring(2, sizeText.indexOf(')')));
			
			// version détaillée
			return 1; // TODO
			
		case FLOAT:
			return 1;// TODO
		}
		
		return 0;
	}
	
	/**
	 * Lis la copy et le fichier de données pour déterminer les valeurs du fichier de données
	 */
	public void parseData(){
		
		// interpretation de la copy
		ArrayList<DataInfo> copyInfo = parseCopy();
		
		// initialisation de la première ligne
		ArrayList<ArrayList<String>> donneesInterpretees = new ArrayList<ArrayList<String>>(); // création du tableau
		donneesInterpretees.add(new ArrayList<String>()); // création de la ligne d'entête
		for(DataInfo ci : copyInfo) {
			donneesInterpretees.get(0).add(ci.getName());
		}
		
		try {
			// Ouverture du fichier de données
			BufferedReader dataReader;
			dataReader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));

			// Boucle sur chaque ligne
			String dataLine;
			int numLigne = 0;
			dataLine = dataReader.readLine();
			while (dataLine != null) {
				// incrémentation du nombre de lignes
				numLigne++;

				// ajout d'une ligne dans les données interpretées
				donneesInterpretees.add(new ArrayList<String>());
				
				// on parcours les données de la copy et on les interprete
				for(DataInfo ci : copyInfo) {
					donneesInterpretees.get(numLigne).add(dataLine.substring(ci.getPosition() - 1, ci.getSize() + ci.getPosition() - 1));
				}
				
				// lecture de la prochaine ligne
				dataLine = dataReader.readLine();
			}
			
			// fermeture du fichier
			dataReader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// envoi des données au panneau de données pour affichage
		dataPanel.setStructuredData(donneesInterpretees);
	}
}
